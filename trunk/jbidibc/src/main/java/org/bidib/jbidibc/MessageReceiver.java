package org.bidib.jbidibc;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bidib.jbidibc.core.BidibMessageProcessor;
import org.bidib.jbidibc.enumeration.BoosterState;
import org.bidib.jbidibc.enumeration.CommandStationProgState;
import org.bidib.jbidibc.enumeration.CommandStationState;
import org.bidib.jbidibc.enumeration.IdentifyState;
import org.bidib.jbidibc.enumeration.LcOutputType;
import org.bidib.jbidibc.exception.NodeAlreadyRegisteredException;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.message.AccessoryNotifyResponse;
import org.bidib.jbidibc.message.AccessoryStateResponse;
import org.bidib.jbidibc.message.BidibMessage;
import org.bidib.jbidibc.message.BoostDiagnosticResponse;
import org.bidib.jbidibc.message.BoostStatResponse;
import org.bidib.jbidibc.message.CommandStationAccessoryAcknowledgeResponse;
import org.bidib.jbidibc.message.CommandStationAccessoryManualResponse;
import org.bidib.jbidibc.message.CommandStationDriveManualResponse;
import org.bidib.jbidibc.message.CommandStationProgStateResponse;
import org.bidib.jbidibc.message.CommandStationStateResponse;
import org.bidib.jbidibc.message.FeedbackAccessoryResponse;
import org.bidib.jbidibc.message.FeedbackAddressResponse;
import org.bidib.jbidibc.message.FeedbackConfidenceResponse;
import org.bidib.jbidibc.message.FeedbackCvResponse;
import org.bidib.jbidibc.message.FeedbackDynStateResponse;
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
import org.bidib.jbidibc.utils.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The message receiver is responsible for creating the messages based on the received bytes from the stream. It is
 * created and initialized by the (default) Bidib implementation.
 */
public class MessageReceiver implements BidibMessageProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageReceiver.class.getName());

    protected static final Logger MSG_RX_LOGGER = LoggerFactory.getLogger("RX");

    protected static final Logger MSG_RAW_LOGGER = LoggerFactory.getLogger("RAW");

    private final Set<MessageListener> messageListeners = Collections
        .synchronizedSet(new LinkedHashSet<MessageListener>());

    private final Collection<NodeListener> nodeListeners = Collections.synchronizedList(new LinkedList<NodeListener>());

    protected AtomicBoolean running = new AtomicBoolean();

    private NodeFactory nodeFactory;

    public MessageReceiver(NodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
        this.nodeFactory.setMessageReceiver(this);

        // enable the running flag
        running.set(true);
    }

    @Override
    public String getErrorInformation() {
        return null;
    }

    @Override
    public synchronized void processMessages(final ByteArrayOutputStream output) throws ProtocolException {
        // if a CRC error is detected in splitMessages the reading loop will terminate ...
        for (byte[] messageArray : MessageUtils.splitBidibMessages(output.toByteArray())) {
            BidibMessage message = null;

            try {
                message = ResponseFactory.create(messageArray);
                if (MSG_RX_LOGGER.isInfoEnabled()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(ByteUtils.bytesToHex(messageArray));
                    MSG_RX_LOGGER.info("receive {} : {}", message, sb);
                }
                if (message != null) {
                    BidibNode bidibNode = null;
                    // some messages are notified directly to listeners
                    int type = ByteUtils.getInt(message.getType());
                    switch (type) {
                        case BidibLibrary.MSG_BOOST_DIAGNOSTIC:
                            fireBoosterCurrent(message.getAddr(), ((BoostDiagnosticResponse) message).getCurrent());
                            fireBoosterTemperature(message.getAddr(),
                                ((BoostDiagnosticResponse) message).getTemperature());
                            fireBoosterVoltage(message.getAddr(), ((BoostDiagnosticResponse) message).getVoltage());
                            break;
                        case BidibLibrary.MSG_BOOST_STAT:
                            fireBoosterState(message.getAddr(), ((BoostStatResponse) message).getState());
                            break;
                        case BidibLibrary.MSG_BM_ADDRESS:
                            fireAddress(message.getAddr(), ((FeedbackAddressResponse) message).getDetectorNumber(),
                                ((FeedbackAddressResponse) message).getAddresses());
                            break;
                        case BidibLibrary.MSG_BM_CONFIDENCE:
                            fireConfidence(message.getAddr(), ((FeedbackConfidenceResponse) message).getValid(),
                                ((FeedbackConfidenceResponse) message).getFreeze(),
                                ((FeedbackConfidenceResponse) message).getSignal());
                            break;
                        case BidibLibrary.MSG_BM_FREE:
                            // acknowledge message
                            nodeFactory.getNode(new Node(message.getAddr())).acknowledgeFree(
                                ((FeedbackFreeResponse) message).getDetectorNumber());

                            fireFree(message.getAddr(), ((FeedbackFreeResponse) message).getDetectorNumber());
                            break;
                        case BidibLibrary.MSG_BM_MULTIPLE:
                            int baseAddress = ((FeedbackMultipleResponse) message).getBaseAddress();
                            int size = ((FeedbackMultipleResponse) message).getSize();
                            byte[] detectorData = ((FeedbackMultipleResponse) message).getDetectorData();

                            LOGGER.debug(
                                "Received FeedbackMultipleResponse, baseAddress: {}, size: {}, detectorData: {}",
                                baseAddress, size, detectorData);
                            // acknowledge message
                            nodeFactory.getNode(new Node(message.getAddr())).acknowledgeMultiple(baseAddress, size,
                                detectorData);

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

                            fireOccupied(message.getAddr(), ((FeedbackOccupiedResponse) message).getDetectorNumber());
                            break;
                        case BidibLibrary.MSG_BM_SPEED:
                            fireSpeed(message.getAddr(), ((FeedbackSpeedResponse) message).getAddress(),
                                ((FeedbackSpeedResponse) message).getSpeed());
                            break;
                        case BidibLibrary.MSG_BM_DYN_STATE:
                            fireDynState(message.getAddr(), ((FeedbackDynStateResponse) message).getDetectorNumber(),
                                ((FeedbackDynStateResponse) message).getDynNumber(),
                                ((FeedbackDynStateResponse) message).getDynValue());
                            break;
                        case BidibLibrary.MSG_ACCESSORY_STATE:
                            // process the AccessoryStateResponse message
                            AccessoryStateResponse accessoryStateResponse = (AccessoryStateResponse) message;

                            fireAccessoryState(message.getAddr(), accessoryStateResponse.getAccessoryState());
                            break;
                        case BidibLibrary.MSG_ACCESSORY_NOTIFY:
                            // process the AccessoryNotifyResponse message
                            bidibNode = nodeFactory.getNode(new Node(message.getAddr()));
                            if (bidibNode instanceof AccessoryNode) {
                                ((AccessoryNode) bidibNode)
                                    .acknowledgeAccessoryState(((AccessoryNotifyResponse) message).getAccessoryState());
                            }

                            AccessoryNotifyResponse accessoryNotifyResponse = (AccessoryNotifyResponse) message;

                            fireAccessoryState(message.getAddr(), accessoryNotifyResponse.getAccessoryState());
                            break;
                        case BidibLibrary.MSG_LC_KEY:
                            fireKey(message.getAddr(), ((LcKeyResponse) message).getKeyNumber(),
                                ((LcKeyResponse) message).getKeyState());
                            break;
                        case BidibLibrary.MSG_LC_STAT:
                            // ignored
                            LOGGER.debug("Received LcStatResponse: {}", message);
                            LcStatResponse lcStatResponse = (LcStatResponse) message;
                            fireLcStat(message.getAddr(), lcStatResponse.getPortType(), lcStatResponse.getPortNumber(),
                                lcStatResponse.getPortStatus());
                            break;
                        case BidibLibrary.MSG_LC_WAIT:
                            LOGGER.info("Received LcWaitResponse: {}", message);
                            // TODO I think this does not work if the sender is already waiting for a response ...
                            // setTimeout(((LcWaitResponse) message).getTimeout());
                            LcWaitResponse lcWaitResponse = (LcWaitResponse) message;
                            fireLcWait(message.getAddr(), lcWaitResponse.getPortType(), lcWaitResponse.getPortNumber(),
                                lcWaitResponse.getPredictedRotationTime());
                            break;
                        case BidibLibrary.MSG_LOGON:
                            // ignored
                            break;
                        case BidibLibrary.MSG_NODE_NEW:
                            Node node = ((NodeNewResponse) message).getNode(message.getAddr());

                            LOGGER.info("Send node changed acknowledge for nodetab version: {}", node.getVersion());

                            boolean fireNodeNew = false;
                            try {
                                // create and register the new node in the node factory because we might receive
                                // spontaneous messages
                                BidibNode newNode = nodeFactory.createNode(node);
                                LOGGER.info("The node factory has registered the new node: {}", newNode);
                                fireNodeNew = true;
                            }
                            catch (NodeAlreadyRegisteredException ex) {
                                LOGGER
                                    .warn(
                                        "The new node is already registered in the nodes list. Signal new node to application is skipped.",
                                        ex);
                            }
                            finally {
                                // acknowledge the new nodetab version to the interface
                                nodeFactory.findNode(message.getAddr()).acknowledgeNodeChanged(node.getVersion());
                            }
                            if (fireNodeNew) {
                                LOGGER.info("Signal new node in system to the application.");
                                fireNodeNew(node);
                            }
                            break;
                        case BidibLibrary.MSG_NODE_LOST:
                            node = ((NodeLostResponse) message).getNode(message.getAddr());

                            BidibNode lostNode = nodeFactory.findNode(message.getAddr());

                            fireNodeLost(node);

                            // acknowledge the new nodetab version to the interface
                            // BidibNode lostNode = nodeFactory.findNode(message.getAddr());
                            if (lostNode != null) {
                                lostNode.acknowledgeNodeChanged(node.getVersion());
                            }
                            else {
                                LOGGER
                                    .warn("Lost node was not registered in node factory, addr: {}", message.getAddr());
                            }
                            break;
                        case BidibLibrary.MSG_SYS_ERROR:
                            SysErrorResponse errorResponse = (SysErrorResponse) message;
                            switch (errorResponse.getErrorCode()) {
                                case BidibLibrary.BIDIB_ERR_IDDOUBLE:
                                    LOGGER.warn("A node attempted to register with an already registered ID: {}",
                                        errorResponse.getAddr());

                                    // TODO forward to application!
                                    break;
                                default:
                                    fireError(message.getAddr(), errorResponse.getErrorCode(),
                                        errorResponse.getReasonData());
                                    break;
                            }

                            // TODO if we have fired an error we should release a possible thread that is waiting for a
                            // result in the receive queue ...
                            // messageReceived(message);
                            break;
                        case BidibLibrary.MSG_SYS_IDENTIFY_STATE:
                            fireIdentify(message.getAddr(), ((SysIdentifyResponse) message).getState());
                            break;
                        case BidibLibrary.MSG_NEW_DECODER:
                            LOGGER.warn("MSG_NEW_DECODER is currently not processed by application: {}", message);
                            break;
                        case BidibLibrary.MSG_ID_SEARCH_ACK:
                            LOGGER.warn("MSG_ID_SEARCH_ACK is currently not processed by application: {}", message);
                            break;
                        case BidibLibrary.MSG_ADDR_CHANGE_ACK:
                            LOGGER.warn("MSG_ADDR_CHANGE_ACK is currently not processed by application: {}", message);
                            break;
                        case BidibLibrary.MSG_BM_CURRENT:
                            LOGGER.warn("MSG_BM_CURRENT is currently not processed by application: {}", message);
                            break;
                        case BidibLibrary.MSG_BM_ACCESSORY:
                            fireBmAccessory(message.getAddr(),
                                ((FeedbackAccessoryResponse) message).getDetectorNumber(),
                                ((FeedbackAccessoryResponse) message).getAddress());
                            break;
                        case BidibLibrary.MSG_BM_CV:
                            fireBmCv(message.getAddr(), ((FeedbackCvResponse) message).getAddress(),
                                ((FeedbackCvResponse) message).getCvNumber(), ((FeedbackCvResponse) message).getDat());
                            break;
                        case BidibLibrary.MSG_BM_BLOCK_CV:
                            LOGGER.warn("MSG_BM_BLOCK_CV is currently not processed by application: {}", message);
                            break;
                        case BidibLibrary.MSG_CS_PROG_STATE:
                            CommandStationProgStateResponse commandStationProgStateResponse =
                                (CommandStationProgStateResponse) message;
                            fireCsProgState(message.getAddr(), commandStationProgStateResponse.getState(),
                                commandStationProgStateResponse.getRemainingTime(),
                                commandStationProgStateResponse.getCvNumber(),
                                commandStationProgStateResponse.getCvData());
                            break;
                        case BidibLibrary.MSG_CS_STATE:
                            CommandStationStateResponse commandStationStateResponse =
                                (CommandStationStateResponse) message;
                            fireCsState(message.getAddr(), commandStationStateResponse.getState());
                            break;
                        case BidibLibrary.MSG_CS_DRIVE_MANUAL:
                            CommandStationDriveManualResponse commandStationDriveManualResponse =
                                (CommandStationDriveManualResponse) message;
                            fireCsDriveManual(commandStationDriveManualResponse);
                            break;
                        case BidibLibrary.MSG_CS_ACCESSORY_ACK:
                            CommandStationAccessoryAcknowledgeResponse commandStationAccessoryAcknowledgeResponse =
                                (CommandStationAccessoryAcknowledgeResponse) message;
                            fireCsAccessoryAck(commandStationAccessoryAcknowledgeResponse);
                            break;
                        case BidibLibrary.MSG_CS_ACCESSORY_MANUAL:
                            CommandStationAccessoryManualResponse commandStationAccessoryManualResponse =
                                (CommandStationAccessoryManualResponse) message;
                            fireCsAccessoryManual(commandStationAccessoryManualResponse);
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
                    if (nodeFactory.getNode(node).getNodeMagic() != null || message instanceof SysMagicResponse) {
                        int numExpected = nodeFactory.getNode(node).getNextReceiveMsgNum(message);
                        int numReceived = message.getNum();
                        LOGGER.trace("Compare the message numbers, expected: {}, received: {}", numExpected,
                            numReceived);
                        if (numReceived != numExpected) {

                            LOGGER.warn("Received wrong message number for message: {}, expected: {}, node: {}",
                                new Object[] { message, numExpected, node });
                            throw new ProtocolException("wrong message number: expected " + numExpected + " but got "
                                + numReceived);
                        }
                    }
                    else {
                        LOGGER.warn("Ignore compare message number because the magic is not set on the node.");
                    }
                }
            }
            output.reset();
        }
    }

    protected void messageReceived(BidibMessage message) {
        // put the message into the receiveQueue because somebody waits for it ...

        BidibNode node = nodeFactory.getNode(new Node(message.getAddr()));
        LOGGER.trace("Offer received message: {} to node: {}", message, node);
        try {
            node.getReceiveQueue().offer(message);
        }
        catch (Exception ex) {
            LOGGER.error("Offer received message to node failed. Message was: " + message, ex);
        }
    }

    @Override
    public void addMessageListener(MessageListener messageListener) {
        synchronized (messageListeners) {
            messageListeners.add(messageListener);
        }
    }

    @Override
    public void addNodeListener(NodeListener nodeListener) {
        synchronized (nodeListeners) {
            nodeListeners.add(nodeListener);
        }
    }

    @Override
    public void removeNodeListener(NodeListener nodeListener) {
        synchronized (nodeListeners) {
            nodeListeners.remove(nodeListener);
        }
    }

    @Override
    public void removeMessageListener(MessageListener messageListener) {
        synchronized (messageListeners) {
            messageListeners.remove(messageListener);
        }
    }

    public void clearMessageListeners() {
        synchronized (messageListeners) {
            messageListeners.clear();
        }
    }

    public void clearNodeListeners() {
        synchronized (nodeListeners) {
            nodeListeners.clear();
        }
    }

    public void disable() {
        LOGGER.debug("disable is called.");
        running.set(false);
    }

    public void enable() {
        LOGGER.debug("enable is called.");
        running.set(true);
    }

    protected void fireAddress(byte[] address, int detectorNumber, Collection<AddressData> addresses) {
        synchronized (messageListeners) {
            for (MessageListener l : messageListeners) {
                l.address(address, detectorNumber, addresses);
            }
        }
    }

    protected void fireBmAccessory(byte[] address, int detectorNumber, int accessoryAddress) {
        synchronized (messageListeners) {
            for (MessageListener l : messageListeners) {
                l.feedbackAccessory(address, detectorNumber, accessoryAddress);
            }
        }
    }

    protected void fireBmCv(byte[] address, AddressData decoderAddress, int cvNumber, int dat) {
        synchronized (messageListeners) {
            for (MessageListener l : messageListeners) {
                l.feedbackCv(address, decoderAddress, cvNumber, dat);
            }
        }
    }

    protected void fireCsProgState(
        byte[] address, CommandStationProgState commandStationProgState, int remainingTime, int cvNumber, int cvData) {
        synchronized (messageListeners) {
            for (MessageListener l : messageListeners) {
                l.csProgState(address, commandStationProgState, remainingTime, cvNumber, cvData);
            }
        }
    }

    protected void fireCsState(byte[] address, CommandStationState commandStationState) {
        synchronized (messageListeners) {
            for (MessageListener l : messageListeners) {
                l.csState(address, commandStationState);
            }
        }
    }

    protected void fireCsDriveManual(CommandStationDriveManualResponse commandStationDriveManualResponse) {
        synchronized (messageListeners) {
            for (MessageListener l : messageListeners) {
                l.csDriveManual(commandStationDriveManualResponse.getAddr(),
                    commandStationDriveManualResponse.getDriveState());
            }
        }
    }

    protected void fireCsAccessoryManual(CommandStationAccessoryManualResponse commandStationAccessoryManualResponse) {
        synchronized (messageListeners) {
            for (MessageListener l : messageListeners) {
                l.csAccessoryManual(commandStationAccessoryManualResponse.getAddr(),
                    commandStationAccessoryManualResponse.getDecoderAddress(),
                    commandStationAccessoryManualResponse.getActivate(),
                    commandStationAccessoryManualResponse.getAspect());
            }
        }
    }

    protected void fireCsAccessoryAck(
        CommandStationAccessoryAcknowledgeResponse commandStationAccessoryAcknowledgeResponse) {
        synchronized (messageListeners) {
            for (MessageListener l : messageListeners) {
                l.csAccessoryAcknowledge(commandStationAccessoryAcknowledgeResponse.getAddr(),
                    commandStationAccessoryAcknowledgeResponse.getAddress(),
                    commandStationAccessoryAcknowledgeResponse.getAcknState());
            }
        }
    }

    protected void fireBoosterCurrent(byte[] address, int current) {
        synchronized (messageListeners) {
            for (MessageListener l : messageListeners) {
                l.boosterCurrent(address, current);
            }
        }
    }

    protected void fireBoosterState(byte[] address, BoosterState state) {
        synchronized (messageListeners) {
            for (MessageListener l : messageListeners) {
                l.boosterState(address, state);
            }
        }
    }

    protected void fireBoosterTemperature(byte[] address, int temperature) {
        synchronized (messageListeners) {
            for (MessageListener l : messageListeners) {
                l.boosterTemperature(address, temperature);
            }
        }
    }

    protected void fireBoosterVoltage(byte[] address, int voltage) {
        synchronized (messageListeners) {
            for (MessageListener l : messageListeners) {
                l.boosterVoltage(address, voltage);
            }
        }
    }

    protected void fireConfidence(byte[] address, int valid, int freeze, int signal) {
        synchronized (messageListeners) {
            for (MessageListener l : messageListeners) {
                l.confidence(address, valid, freeze, signal);
            }
        }
    }

    private void fireFree(byte[] address, int detectorNumber) {
        synchronized (messageListeners) {
            for (MessageListener l : messageListeners) {
                l.free(address, detectorNumber);
            }
        }
    }

    private void fireIdentify(byte[] address, IdentifyState identifyState) {
        LOGGER.debug("Identify, addr: {}, identifyState: {}", address, identifyState);
        synchronized (messageListeners) {
            for (MessageListener l : messageListeners) {
                l.identity(address, identifyState);
            }
        }
    }

    private void fireKey(byte[] address, int keyNumber, int keyState) {
        synchronized (messageListeners) {
            for (MessageListener l : messageListeners) {
                l.key(address, keyNumber, keyState);
            }
        }
    }

    private void fireLcStat(byte[] address, LcOutputType portType, int portNumber, int portStatus) {
        synchronized (messageListeners) {
            for (MessageListener l : messageListeners) {
                l.lcStat(address, portType, portNumber, portStatus);
            }
        }
    }

    private void fireLcWait(byte[] address, LcOutputType portType, int portNumber, int predRotationTime) {
        synchronized (messageListeners) {
            for (MessageListener l : messageListeners) {
                l.lcWait(address, portType, portNumber, predRotationTime);
            }
        }
    }

    private void fireNodeLost(Node node) {
        synchronized (nodeListeners) {
            for (NodeListener l : nodeListeners) {
                l.nodeLost(node);
            }
        }
    }

    private void fireNodeNew(Node node) {
        synchronized (nodeListeners) {
            for (NodeListener l : nodeListeners) {
                l.nodeNew(node);
            }
        }
    }

    private void fireOccupied(byte[] address, int detectorNumber) {
        synchronized (messageListeners) {
            for (MessageListener l : messageListeners) {
                l.occupied(address, detectorNumber);
            }
        }
    }

    protected void fireSpeed(byte[] address, AddressData addressData, int speed) {
        synchronized (messageListeners) {
            for (MessageListener l : messageListeners) {
                l.speed(address, addressData, speed);
            }
        }
    }

    private void fireError(byte[] address, int errorCode, byte[] reasonData) {
        LOGGER.error("Error received from system, addr: {}, errorCode: {}, reasonData: {}", address, errorCode,
            reasonData);
        synchronized (messageListeners) {
            for (MessageListener l : messageListeners) {
                l.error(address, errorCode, reasonData);
            }
        }
    }

    private void fireAccessoryState(byte[] address, AccessoryState accessoryState) {
        synchronized (messageListeners) {
            for (MessageListener l : messageListeners) {
                l.accessoryState(address, accessoryState);
            }
        }
    }

    protected void fireDynState(byte[] address, int detectorNumber, int dynNumber, int dynValue) {
        // populate the dynamic state ...
        synchronized (messageListeners) {
            for (MessageListener l : messageListeners) {
                l.dynState(address, detectorNumber, dynNumber, dynValue);
            }
        }
    }

    /**
     * Remove an orphan node. If the node does not disconnect according to specification or the node is an interface
     * node during update this update this can lead to orphan nodes.
     * 
     * @param node
     *            the node to be removed
     */
    public void removeOrphanNode(Node node) {
        LOGGER.info("Remove orphan node: {}", node);
        fireNodeLost(node);
    }
}
