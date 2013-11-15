package org.bidib.jbidibc;

import gnu.io.SerialPort;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import org.bidib.jbidibc.enumeration.BoosterState;
import org.bidib.jbidibc.enumeration.IdentifyState;
import org.bidib.jbidibc.enumeration.LcOutputType;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.message.AccessoryStateResponse;
import org.bidib.jbidibc.message.BidibMessage;
import org.bidib.jbidibc.message.BoostCurrentResponse;
import org.bidib.jbidibc.message.BoostDiagnosticResponse;
import org.bidib.jbidibc.message.BoostStatResponse;
import org.bidib.jbidibc.message.FeedbackAddressResponse;
import org.bidib.jbidibc.message.FeedbackConfidenceResponse;
import org.bidib.jbidibc.message.FeedbackFreeResponse;
import org.bidib.jbidibc.message.FeedbackMultipleResponse;
import org.bidib.jbidibc.message.FeedbackOccupiedResponse;
import org.bidib.jbidibc.message.FeedbackSpeedResponse;
import org.bidib.jbidibc.message.LcKeyResponse;
import org.bidib.jbidibc.message.LcStatResponse;
import org.bidib.jbidibc.message.LcWaitResponse;
import org.bidib.jbidibc.message.NodeLostResponse;
import org.bidib.jbidibc.message.NodeNewResponse;
import org.bidib.jbidibc.message.ResponseFactory;
import org.bidib.jbidibc.message.SysErrorResponse;
import org.bidib.jbidibc.message.SysIdentifyResponse;
import org.bidib.jbidibc.message.SysMagicResponse;
import org.bidib.jbidibc.node.AccessoryNode;
import org.bidib.jbidibc.node.BidibNode;
import org.bidib.jbidibc.node.NodeFactory;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The message receiver is responsible for creating the messages based on the received bytes from the stream.
 * It is created and initialized by the (default) Bidib implementation.
 */
public class MessageReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageReceiver.class.getName());

    private static final Logger MSG_RX_LOGGER = LoggerFactory.getLogger("RX");

    //    private static final Logger MSG_RAW_LOGGER = LoggerFactory.getLogger("RAW");

    private final Collection<MessageListener> messageListeners =
        Collections.synchronizedList(new LinkedList<MessageListener>());

    private final Collection<NodeListener> nodeListeners = Collections.synchronizedList(new LinkedList<NodeListener>());

    private boolean running;

    private NodeFactory nodeFactory;

    private BidibInterface bidib;

    protected MessageReceiver(NodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
        running = true;
        nodeFactory.setMessageReceiver(this);
    }

    public void setBidib(BidibInterface bidib) {
        this.bidib = bidib;
    }

    /**
     * Receive messages from the configured port
     */
    public void receive(final SerialPort port) {
        LOGGER.debug("Start receiving messages.");

        synchronized (this) {
            LOGGER.debug("Starting message receiver.");
            try {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                InputStream input = null;

                if (port != null) {
                    input = port.getInputStream();
                }
                if (input != null) {
                    int data = 0;
                    boolean escapeHot = false;
                    StringBuilder logRecord = new StringBuilder();

                    while (running && (data = input.read()) != -1) {
                        if (LOGGER.isTraceEnabled()) {
                            LOGGER.trace("received data: {}", ByteUtils.byteToHex(data));
                        }
                        // append data to log record
                        logRecord.append(ByteUtils.byteToHex(data)).append(" ");

                        // check if the current is the end of a packet
                        if (data == BidibLibrary.BIDIB_PKT_MAGIC && output.size() > 0) {

                            LOGGER.debug("Received raw message: {}", logRecord);
                            //                            MSG_RAW_LOGGER.info("raw message: {}", logRecord);
                            logRecord.setLength(0);

                            // if a CRC error is detected in splitMessages the reading loop will terminate ...
                            for (byte[] messageArray : splitMessages(output.toByteArray())) {
                                BidibMessage message = null;

                                try {
                                    message = ResponseFactory.create(messageArray);
                                    if (MSG_RX_LOGGER.isInfoEnabled()) {
                                        StringBuilder sb = new StringBuilder();
                                        sb.append(ByteUtils.bytesToHex(messageArray));
                                        MSG_RX_LOGGER.info("receive {} : {}", message, sb);
                                    }
                                    if (message != null) {

                                        // some messages are notified directly to listeners
                                        int type = ByteUtils.getInt(message.getType());
                                        switch (type) {
                                            case BidibLibrary.MSG_BOOST_CURRENT:
                                                fireBoosterCurrent(message.getAddr(), ((BoostCurrentResponse) message)
                                                    .getCurrent());
                                                break;
                                            case BidibLibrary.MSG_BOOST_DIAGNOSTIC:
                                                fireBoosterCurrent(message.getAddr(),
                                                    ((BoostDiagnosticResponse) message).getCurrent());
                                                fireBoosterTemperature(message.getAddr(),
                                                    ((BoostDiagnosticResponse) message).getTemperature());
                                                fireBoosterVoltage(message.getAddr(),
                                                    ((BoostDiagnosticResponse) message).getVoltage());
                                                break;
                                            case BidibLibrary.MSG_BOOST_STAT:
                                                fireBoosterState(message.getAddr(), ((BoostStatResponse) message)
                                                    .getState());
                                                break;
                                            case BidibLibrary.MSG_BM_ADDRESS:
                                                fireAddress(message.getAddr(), ((FeedbackAddressResponse) message)
                                                    .getDetectorNumber(), ((FeedbackAddressResponse) message)
                                                    .getAddresses());
                                                break;
                                            case BidibLibrary.MSG_BM_CONFIDENCE:
                                                fireConfidence(message.getAddr(),
                                                    ((FeedbackConfidenceResponse) message).getValid(),
                                                    ((FeedbackConfidenceResponse) message).getFreeze(),
                                                    ((FeedbackConfidenceResponse) message).getSignal());
                                                break;
                                            case BidibLibrary.MSG_BM_FREE:
                                                // acknowledge message
                                                nodeFactory.getNode(new Node(message.getAddr())).acknowledgeFree(
                                                    ((FeedbackFreeResponse) message).getDetectorNumber());

                                                fireFree(message.getAddr(), ((FeedbackFreeResponse) message)
                                                    .getDetectorNumber());
                                                break;
                                            case BidibLibrary.MSG_BM_MULTIPLE:
                                                int baseAddress = ((FeedbackMultipleResponse) message).getBaseAddress();
                                                int size = ((FeedbackMultipleResponse) message).getSize();
                                                byte[] detectorData =
                                                    ((FeedbackMultipleResponse) message).getDetectorData();

                                                LOGGER
                                                    .debug(
                                                        "Received FeedbackMultipleResponse, baseAddress: {}, size: {}, detectorData: {}",
                                                        baseAddress, size, detectorData);
                                                // acknowledge message
                                                nodeFactory.getNode(new Node(message.getAddr())).acknowledgeMultiple(
                                                    baseAddress, size, detectorData);

                                                int offset = 0;

                                                for (byte detectorByte : detectorData) {
                                                    if (offset >= size) {
                                                        break;
                                                    }
                                                    for (int bit = 0; bit <= 7; bit++) {
                                                        if (((detectorByte >> bit) & 1) == 1) {
                                                            fireOccupied(message.getAddr(), baseAddress + offset);
                                                        }
                                                        else {
                                                            fireFree(message.getAddr(), baseAddress + offset);
                                                        }
                                                        offset++;
                                                    }
                                                }
                                                break;
                                            case BidibLibrary.MSG_BM_OCC:
                                                // acknowledge message
                                                nodeFactory.getNode(new Node(message.getAddr())).acknowledgeOccupied(
                                                    ((FeedbackOccupiedResponse) message).getDetectorNumber());

                                                fireOccupied(message.getAddr(), ((FeedbackOccupiedResponse) message)
                                                    .getDetectorNumber());
                                                break;
                                            case BidibLibrary.MSG_BM_SPEED:
                                                fireSpeed(message.getAddr(), ((FeedbackSpeedResponse) message)
                                                    .getAddress(), ((FeedbackSpeedResponse) message).getSpeed());
                                                break;
                                            case BidibLibrary.MSG_ACCESSORY_STATE:
                                                // process the AccessoryStateResponse message
                                                BidibNode bidibNode = nodeFactory.getNode(new Node(message.getAddr()));
                                                if (bidibNode instanceof AccessoryNode) {
                                                    ((AccessoryNode) bidibNode)
                                                        .acknowledgeAccessoryState(((AccessoryStateResponse) message)
                                                            .getAccessoryState());
                                                }

                                                AccessoryStateResponse accessoryStateResponse =
                                                    (AccessoryStateResponse) message;

                                                fireAccessoryState(message.getAddr(), accessoryStateResponse
                                                    .getAccessoryState());
                                                break;
                                            case BidibLibrary.MSG_LC_KEY:
                                                fireKey(message.getAddr(), ((LcKeyResponse) message).getKeyNumber(),
                                                    ((LcKeyResponse) message).getKeyState());
                                                break;
                                            case BidibLibrary.MSG_LC_STAT:
                                                // ignored
                                                LOGGER.debug("Received LcStatResponse: {}", message);
                                                LcStatResponse lcStatResponse = (LcStatResponse) message;
                                                fireLcStat(message.getAddr(), lcStatResponse.getPortType(),
                                                    lcStatResponse.getPortNumber(), lcStatResponse.getPortStatus());
                                                break;
                                            case BidibLibrary.MSG_LC_WAIT:
                                                LOGGER.info("Received LcWaitResponse: {}", message);
                                                // TODO I think this does not work if the sender is already waiting for a response ...
                                                setTimeout(((LcWaitResponse) message).getTimeout());
                                                break;
                                            case BidibLibrary.MSG_LOGON:
                                                // ignored
                                                break;
                                            case BidibLibrary.MSG_NODE_NEW:
                                                Node node = ((NodeNewResponse) message).getNode(message.getAddr());

                                                LOGGER.info("Send node changed acknowledge for nodetab version: {}",
                                                    node.getVersion());

                                                // create and register the new node in the node factory because we might receive spontaneous messages
                                                nodeFactory.createNode(node);

                                                // acknowledge the new nodetab version to the interface
                                                nodeFactory.getRootNode().acknowledgeNodeChanged(node.getVersion());

                                                fireNodeNew(node);
                                                break;
                                            case BidibLibrary.MSG_NODE_LOST:
                                                node = ((NodeLostResponse) message).getNode(message.getAddr());

                                                fireNodeLost(node);

                                                // acknowledge the new nodetab version to the interface
                                                nodeFactory.getRootNode().acknowledgeNodeChanged(node.getVersion());
                                                break;
                                            case BidibLibrary.MSG_SYS_ERROR:
                                                SysErrorResponse errorResponse = (SysErrorResponse) message;
                                                switch (errorResponse.getErrorCode()) {
                                                    case BidibLibrary.BIDIB_ERR_IDDOUBLE:
                                                        LOGGER
                                                            .warn(
                                                                "A node attempted to register with an already registered ID: {}",
                                                                errorResponse.getAddr());

                                                        // TODO forward to application!
                                                        break;
                                                    default:
                                                        fireError(message.getAddr(), errorResponse.getErrorCode());
                                                        break;
                                                }

                                                // TODO if we have fired an error we should release a possible thread that is waiting for a result in the receive queue ...
                                                //                                        messageReceived(message);
                                                break;
                                            case BidibLibrary.MSG_SYS_IDENTIFY_STATE:
                                                fireIdentify(message.getAddr(), ((SysIdentifyResponse) message)
                                                    .getState());
                                                break;
                                            case BidibLibrary.MSG_NEW_DECODER:
                                                LOGGER.warn(
                                                    "MSG_NEW_DECODER is currently not processed by application: {}",
                                                    message);
                                                break;
                                            case BidibLibrary.MSG_ID_SEARCH_ACK:
                                                LOGGER.warn(
                                                    "MSG_ID_SEARCH_ACK is currently not processed by application: {}",
                                                    message);
                                                break;
                                            case BidibLibrary.MSG_ADDR_CHANGE_ACK:
                                                LOGGER
                                                    .warn(
                                                        "MSG_ADDR_CHANGE_ACK is currently not processed by application: {}",
                                                        message);
                                                break;
                                            case BidibLibrary.MSG_BM_CURRENT:
                                                LOGGER.warn(
                                                    "MSG_BM_CURRENT is currently not processed by application: {}",
                                                    message);
                                                break;
                                            default:
                                                messageReceived(message);
                                                break;
                                        }
                                    }
                                    else {
                                        LOGGER.error("Ignore unknown message.");
                                    }
                                }
                                finally {
                                    if (message != null) {
                                        // the message numbers are evaluated only after the magic message on the node was received ...

                                        // verify that the receive message number is valid
                                        Node node = new Node(message.getAddr());
                                        if (nodeFactory.getNode(node).getNodeMagic() != null
                                            || message instanceof SysMagicResponse) {
                                            int numExpected = nodeFactory.getNode(node).getNextReceiveMsgNum(message);
                                            int numReceived = message.getNum();
                                            LOGGER.trace("Compare the message numbers, expected: {}, received: {}",
                                                numExpected, numReceived);
                                            if (numReceived != numExpected) {

                                                LOGGER
                                                    .warn(
                                                        "Received wrong message number for message: {}, expected: {}, node: {}",
                                                        new Object[] { message, numExpected, node });
                                                throw new ProtocolException("wrong message number: expected "
                                                    + numExpected + " but got " + numReceived);
                                            }
                                        }
                                        else {
                                            LOGGER
                                                .warn("Ignore compare message number because the magic is not set on the node.");
                                        }
                                    }
                                }
                                output.reset();
                            }
                        }
                        else {
                            if (data == BidibLibrary.BIDIB_PKT_ESCAPE) {
                                escapeHot = true;
                            }
                            else if (data != BidibLibrary.BIDIB_PKT_MAGIC) {
                                if (escapeHot) {
                                    data ^= 0x20;
                                    escapeHot = false;
                                }
                                output.write((byte) data);
                            }
                        }
                    }
                    LOGGER.debug("Leaving receive loop, RUNNING: {}", running);

                    if (output != null && output.size() > 0) {
                        LOGGER.warn("Data remaining in output: {}", new Object[] { output.toByteArray() });
                    }
                    if (logRecord != null && logRecord.length() > 0) {
                        LOGGER.warn("Data remaining in logRecord: {}", logRecord);
                    }

                }
                else {
                    LOGGER.error("No input available.");
                }

            }
            catch (Exception e) {
                LOGGER.warn("Exception detected in message receiver!", e);
                throw new RuntimeException(e);
            }
        }
    }

    protected void messageReceived(BidibMessage message) {
        // put the message into the receiveQueue because somebody waits for it ...

        LOGGER.trace("Offer received message to node: {}", message);
        BidibNode node = nodeFactory.getNode(new Node(message.getAddr()));
        try {
            node.getReceiveQueue().offer(message);
        }
        catch (Exception ex) {
            LOGGER.error("Offer received message to node failed. Message was: " + message, ex);
        }
    }

    public void addMessageListener(MessageListener messageListener) {
        messageListeners.add(messageListener);
    }

    public void addNodeListener(NodeListener nodeListener) {
        nodeListeners.add(nodeListener);
    }

    public void disable() {
        LOGGER.debug("disable is called.");
        running = false;
    }

    public void enable() {
        LOGGER.debug("enable is called.");
        running = true;
    }

    protected void fireAddress(byte[] address, int detectorNumber, Collection<AddressData> addresses) {
        for (MessageListener l : messageListeners) {
            l.address(address, detectorNumber, addresses);
        }
    }

    protected void fireBoosterCurrent(byte[] address, int current) {
        for (MessageListener l : messageListeners) {
            l.boosterCurrent(address, current);
        }
    }

    protected void fireBoosterState(byte[] address, BoosterState state) {
        for (MessageListener l : messageListeners) {
            l.boosterState(address, state);
        }
    }

    protected void fireBoosterTemperature(byte[] address, int temperature) {
        for (MessageListener l : messageListeners) {
            l.boosterTemperature(address, temperature);
        }
    }

    protected void fireBoosterVoltage(byte[] address, int voltage) {
        for (MessageListener l : messageListeners) {
            l.boosterVoltage(address, voltage);
        }
    }

    protected void fireConfidence(byte[] address, int valid, int freeze, int signal) {
        for (MessageListener l : messageListeners) {
            l.confidence(address, valid, freeze, signal);
        }
    }

    private void fireFree(byte[] address, int detectorNumber) {
        for (MessageListener l : messageListeners) {
            l.free(address, detectorNumber);
        }
    }

    private void fireIdentify(byte[] address, IdentifyState identifyState) {
        LOGGER.debug("Identify, addr: {}, identifyState: {}", address, identifyState);
        for (MessageListener l : messageListeners) {
            l.identity(address, identifyState);
        }
    }

    private void fireKey(byte[] address, int keyNumber, int keyState) {
        for (MessageListener l : messageListeners) {
            l.key(address, keyNumber, keyState);
        }
    }

    private void fireLcStat(byte[] address, LcOutputType portType, int portNumber, int portStatus) {
        for (MessageListener l : messageListeners) {
            l.lcStat(address, portType, portNumber, portStatus);
        }
    }

    private void fireNodeLost(Node node) {
        for (NodeListener l : nodeListeners) {
            l.nodeLost(node);
        }
    }

    private void fireNodeNew(Node node) {
        for (NodeListener l : nodeListeners) {
            l.nodeNew(node);
        }
    }

    private void fireOccupied(byte[] address, int detectorNumber) {
        for (MessageListener l : messageListeners) {
            l.occupied(address, detectorNumber);
        }
    }

    protected void fireSpeed(byte[] address, AddressData addressData, int speed) {
        for (MessageListener l : messageListeners) {
            l.speed(address, addressData, speed);
        }
    }

    private void fireError(byte[] address, int errorCode) {
        LOGGER.error("Error received from system, addr: {}, errorCode: {}", address, errorCode);
        for (MessageListener l : messageListeners) {
            l.error(address, errorCode);
        }
    }

    private void fireAccessoryState(byte[] address, AccessoryState accessoryState) {
        for (MessageListener l : messageListeners) {
            l.accessoryState(address, accessoryState);
        }
    }

    public void removeMessageListener(MessageListener l) {

        messageListeners.remove(l);
    }

    public void setTimeout(int timeout) {
        LOGGER.info("Set the timeout for bidib messages: {}", timeout);
        bidib.setReceiveTimeout(timeout);
    }

    /**
     * Split the byte array into separate messages. The CRC value at the end is calculated over the whole array.
     * 
     * @param output
     *            array containing at least one message
     * 
     * @return list of the separated messages
     * 
     * @throws ProtocolException
     *             Thrown if the CRC failed.
     */
    private static Collection<byte[]> splitMessages(byte[] output) throws ProtocolException {
        Collection<byte[]> result = new LinkedList<byte[]>();
        int index = 0;

        LOGGER.trace("splitMessages: {}", output);

        while (index < output.length) {
            int size = output[index] + 1;

            if (size <= 0) {
                throw new ProtocolException("cannot split messages, array size is " + size);
            }

            byte[] message = new byte[size];

            try {
                System.arraycopy(output, index, message, 0, message.length);
            }
            catch (ArrayIndexOutOfBoundsException ex) {
                LOGGER.warn("Failed to copy, msg.len: " + message.length + ", size: " + size + ", output.len: "
                    + output.length, ex);
                throw ex;
            }
            result.add(message);
            index += size;

            // CRC
            if (index == output.length - 1) {
                int crc = 0;

                for (index = 0; index < output.length - 1; index++) {
                    crc = CRC8.getCrcValue((output[index] ^ crc) & 0xFF);
                }
                if (crc != (output[index] & 0xFF)) {
                    throw new ProtocolException("CRC failed: should be " + crc + " but was " + (output[index] & 0xFF));
                }
                break;
            }
        }
        return result;
    }
}
