package org.bidib.jbidibc;

import gnu.io.SerialPort;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.bidib.jbidibc.enumeration.BoosterState;
import org.bidib.jbidibc.enumeration.IdentifyState;
import org.bidib.jbidibc.exception.ProtocolException;
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
import org.bidib.jbidibc.message.LogonResponse;
import org.bidib.jbidibc.message.NodeLostResponse;
import org.bidib.jbidibc.message.NodeNewResponse;
import org.bidib.jbidibc.message.ResponseFactory;
import org.bidib.jbidibc.message.SysErrorResponse;
import org.bidib.jbidibc.message.SysIdentifyResponse;
import org.bidib.jbidibc.node.NodeFactory;
import org.bidib.jbidibc.utils.ByteUtils;
import org.bidib.jbidibc.utils.CollectionUtils;
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

    private BlockingQueue<BidibMessage> receiveQueue = new LinkedBlockingQueue<BidibMessage>();

    private final Collection<MessageListener> listeners =
        Collections.synchronizedList(new LinkedList<MessageListener>());

    private static int timeout = Bidib.DEFAULT_TIMEOUT;

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

    public void setReceiveQueue(BlockingQueue<BidibMessage> receiveQueue) {
        this.receiveQueue = receiveQueue;
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
                            LOGGER.trace("received data: {}", String.format("%02x ", data));
                        }
                        // append data to log record
                        logRecord.append(String.format("%02x ", data));

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
                                        for (int i = 0; i < messageArray.length; i++) {
                                            sb.append(String.format("%02x ", messageArray[i]));
                                        }
                                        MSG_RX_LOGGER.info("receive {} : {}", message, sb);
                                    }

                                    // some messages are notified directly to listeners
                                    if (message instanceof BoostCurrentResponse) {
                                        fireBoosterCurrent(message.getAddr(), ((BoostCurrentResponse) message)
                                            .getCurrent());
                                    }
                                    else if (message instanceof BoostDiagnosticResponse) {
                                        fireBoosterCurrent(message.getAddr(), ((BoostDiagnosticResponse) message)
                                            .getCurrent());
                                        fireBoosterTemperature(message.getAddr(), ((BoostDiagnosticResponse) message)
                                            .getTemperature());
                                        fireBoosterVoltage(message.getAddr(), ((BoostDiagnosticResponse) message)
                                            .getVoltage());
                                    }
                                    else if (message instanceof BoostStatResponse) {
                                        fireBoosterState(message.getAddr(), ((BoostStatResponse) message).getState());
                                    }
                                    else if (message instanceof FeedbackAddressResponse) {
                                        fireAddress(message.getAddr(), ((FeedbackAddressResponse) message)
                                            .getDetectorNumber(), ((FeedbackAddressResponse) message).getAddresses());
                                    }
                                    else if (message instanceof FeedbackConfidenceResponse) {
                                        fireConfidence(message.getAddr(), ((FeedbackConfidenceResponse) message)
                                            .getValid(), ((FeedbackConfidenceResponse) message).getFreeze(),
                                            ((FeedbackConfidenceResponse) message).getSignal());
                                    }
                                    else if (message instanceof FeedbackFreeResponse) {
                                        // acknowledge message
                                        nodeFactory.getNode(new Node(message.getAddr())).acknowledgeFree(
                                            ((FeedbackFreeResponse) message).getDetectorNumber());

                                        fireFree(message.getAddr(), ((FeedbackFreeResponse) message)
                                            .getDetectorNumber());
                                    }
                                    else if (message instanceof FeedbackMultipleResponse) {
                                        int baseAddress = ((FeedbackMultipleResponse) message).getBaseAddress();
                                        int size = ((FeedbackMultipleResponse) message).getSize();
                                        byte[] detectorData = ((FeedbackMultipleResponse) message).getDetectorData();

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
                                    }
                                    else if (message instanceof FeedbackOccupiedResponse) {
                                        // acknowledge message
                                        nodeFactory.getNode(new Node(message.getAddr())).acknowledgeOccupied(
                                            ((FeedbackOccupiedResponse) message).getDetectorNumber());

                                        fireOccupied(message.getAddr(), ((FeedbackOccupiedResponse) message)
                                            .getDetectorNumber());
                                    }
                                    else if (message instanceof FeedbackSpeedResponse) {
                                        fireSpeed(message.getAddr(), ((FeedbackSpeedResponse) message).getAddress(),
                                            ((FeedbackSpeedResponse) message).getSpeed());
                                    }
                                    else if (message instanceof LcKeyResponse) {
                                        fireKey(message.getAddr(), ((LcKeyResponse) message).getKeyNumber(),
                                            ((LcKeyResponse) message).getKeyState());
                                    }
                                    else if (message instanceof LcStatResponse) {
                                        // ignored
                                    }
                                    else if (message instanceof LcWaitResponse) {
                                        // TODO I think this does not work if the sender is already waiting for a response ...
                                        setTimeout(((LcWaitResponse) message).getTimeout());
                                    }
                                    else if (message instanceof LogonResponse) {
                                        // ignored
                                    }
                                    else if (message instanceof NodeNewResponse) {
                                        Node node = ((NodeNewResponse) message).getNode(message.getAddr());

                                        // TODO for bridge/hub nodes we must use the parent of the new node ???
                                        nodeFactory.getRootNode().acknowledgeNodeChanged(node.getVersion());
                                        fireNodeNew(node);
                                    }
                                    else if (message instanceof NodeLostResponse) {
                                        Node node = ((NodeLostResponse) message).getNode(message.getAddr());

                                        fireNodeLost(node);
                                        // TODO for bridge/hub nodes we must use the parent of the new node ???
                                        nodeFactory.getRootNode().acknowledgeNodeChanged(node.getVersion());
                                        //                                        fireNodeLost(node);
                                    }
                                    else if (message instanceof SysErrorResponse) {
                                        //                                        && ((SysErrorResponse) message).getErrorCode() == BidibLibrary.BIDIB_ERR_IDDOUBLE) {
                                        SysErrorResponse errorResponse = (SysErrorResponse) message;
                                        switch (errorResponse.getErrorCode()) {
                                            case BidibLibrary.BIDIB_ERR_IDDOUBLE:
                                                LOGGER.warn(
                                                    "A node attempted to register with an already registered ID: {}",
                                                    errorResponse.getAddr());

                                                // TODO forward to application!
                                                break;
                                            default:
                                                fireError(message.getAddr(), errorResponse.getErrorCode());
                                                break;
                                        }

                                        // TODO if we have fired an error we should release a possible thread that is waiting for a result in the receive queue ...
                                        messageReceived(message);
                                    }
                                    else if (message instanceof SysIdentifyResponse) {
                                        fireIdentify(message.getAddr(), ((SysIdentifyResponse) message).getState());
                                    }
                                    else {
                                        messageReceived(message);
                                    }
                                }
                                finally {
                                    if (message != null) {
                                        // verify that the receive message number is valid
                                        Node node = new Node(message.getAddr());
                                        int numExpected = nodeFactory.getNode(node).getNextReceiveMsgNum(message);
                                        int numReceived = message.getNum();
                                        LOGGER.debug("Compare the message numbers, expected: {}, received: {}",
                                            numExpected, numReceived);
                                        if (numReceived != numExpected) {
                                            LOGGER
                                                .warn(
                                                    "Received wrong message number for message: {}, expected: {}, node: {}",
                                                    new Object[] { message, numExpected, node });
                                            throw new ProtocolException("wrong message number: expected " + numExpected
                                                + " but got " + numReceived);
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
        LOGGER.debug("Put message to receiveQueue: {}", message);
        receiveQueue.offer(message);
    }

    public void addMessageListener(MessageListener listener) {
        listeners.add(listener);
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
        for (MessageListener l : listeners) {
            l.address(address, detectorNumber, addresses);
        }
    }

    protected void fireBoosterCurrent(byte[] address, int current) {
        for (MessageListener l : listeners) {
            l.boosterCurrent(address, current);
        }
    }

    protected void fireBoosterState(byte[] address, BoosterState state) {
        for (MessageListener l : listeners) {
            l.boosterState(address, state);
        }
    }

    protected void fireBoosterTemperature(byte[] address, int temperature) {
        for (MessageListener l : listeners) {
            l.boosterTemperature(address, temperature);
        }
    }

    protected void fireBoosterVoltage(byte[] address, int voltage) {
        for (MessageListener l : listeners) {
            l.boosterVoltage(address, voltage);
        }
    }

    protected void fireConfidence(byte[] address, int valid, int freeze, int signal) {
        for (MessageListener l : listeners) {
            l.confidence(address, valid, freeze, signal);
        }
    }

    private void fireFree(byte[] address, int detectorNumber) {
        for (MessageListener l : listeners) {
            l.free(address, detectorNumber);
        }
    }

    private void fireIdentify(byte[] address, IdentifyState identifyState) {
        for (MessageListener l : listeners) {
            l.identity(address, identifyState);
        }
    }

    private void fireKey(byte[] address, int keyNumber, int keyState) {
        for (MessageListener l : listeners) {
            l.key(address, keyNumber, keyState);
        }
    }

    private void fireNodeLost(Node node) {
        for (MessageListener l : listeners) {
            l.nodeLost(node);
        }
    }

    private void fireNodeNew(Node node) {
        for (MessageListener l : listeners) {
            l.nodeNew(node);
        }
    }

    private void fireOccupied(byte[] address, int detectorNumber) {
        for (MessageListener l : listeners) {
            l.occupied(address, detectorNumber);
        }
    }

    protected void fireSpeed(byte[] address, AddressData addressData, int speed) {
        for (MessageListener l : listeners) {
            l.speed(address, addressData, speed);
        }
    }

    private void fireError(byte[] address, int errorCode) {
        LOGGER.error("Error received from system, addr: {}, errorCode: {}", address, errorCode);
        for (MessageListener l : listeners) {
            l.error(address, errorCode);
        }
    }

    /**
     * Get a message from the receiveQueue for the defined timeout period.
     * 
     * @param responseTypes the optional list of responseType ids to wait for
     *
     * @return the received message or null if no message was received during the defined period.
     * @throws InterruptedException thrown if wait wait for response is interrupted
     */
    public BidibMessage getMessage(List<Integer> responseTypes) throws InterruptedException {
        LOGGER.debug("get message with responseType: {}", responseTypes);
        BidibMessage result = null;

        // wait a maximum of 3 seconds to recieve message
        long cancelReceiveTs = System.currentTimeMillis() + 3000;
        boolean leaveLoop = false;

        do {
            result = receiveQueue.poll(timeout, TimeUnit.MILLISECONDS);

            long now = System.currentTimeMillis();

            // handling of specific response type
            if (result instanceof SysErrorResponse) {
                LOGGER.warn("Received an error response: {}", result);
                leaveLoop = true;
            }
            else if (result != null && CollectionUtils.hasElements(responseTypes)) {
                BidibMessage response = null;
                for (Integer responseType : responseTypes) {
                    LOGGER.debug("Check if the response type is equal, responseType: {}, response.type: {}",
                        responseType.byteValue(), result.getType());

                    if (responseType == ByteUtils.getInt(result.getType())) {
                        LOGGER.debug("This is the expected response: {}", result);
                        response = result;
                        break;
                    }
                }

                if (response == null) {
                    LOGGER.debug("This is NOT the expected response: {}", result);
                    result = null;
                }

                LOGGER.debug("startReceive: {}, now: {}, result: {}", cancelReceiveTs, now, result);
                if (result != null || cancelReceiveTs < now) {
                    LOGGER.debug("leave loop.");
                    leaveLoop = true;
                }
            }
            // handling if no specific response type expected ...
            else if (result != null || (cancelReceiveTs < now)) {
                LOGGER.debug("leave loop, result: {}", result);
                leaveLoop = true;
            }
        }
        while (!leaveLoop);
        LOGGER.debug("Received message: {}", result);
        return result;
    }

    public void removeMessageListener(MessageListener l) {

        listeners.remove(l);
    }

    public void setTimeout(int timeout) {
        LOGGER.info("Set the timeout for bidib messages: {}", timeout);
        bidib.setReceiveTimeout(timeout);
        MessageReceiver.timeout = timeout;
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

        LOGGER.debug("splitMessages: {}", output);

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
