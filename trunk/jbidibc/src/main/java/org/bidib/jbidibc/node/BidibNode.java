package org.bidib.jbidibc.node;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import org.bidib.jbidibc.Bidib;
import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.CRC8;
import org.bidib.jbidibc.Feature;
import org.bidib.jbidibc.FirmwareUpdateStat;
import org.bidib.jbidibc.MessageReceiver;
import org.bidib.jbidibc.Node;
import org.bidib.jbidibc.ProtocolVersion;
import org.bidib.jbidibc.SoftwareVersion;
import org.bidib.jbidibc.VendorData;
import org.bidib.jbidibc.enumeration.FirmwareUpdateOperation;
import org.bidib.jbidibc.enumeration.IdentifyState;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.message.BidibMessage;
import org.bidib.jbidibc.message.BoostOffMessage;
import org.bidib.jbidibc.message.BoostOnMessage;
import org.bidib.jbidibc.message.BoostQueryMessage;
import org.bidib.jbidibc.message.FeatureCountResponse;
import org.bidib.jbidibc.message.FeatureGetAllMessage;
import org.bidib.jbidibc.message.FeatureGetMessage;
import org.bidib.jbidibc.message.FeatureGetNextMessage;
import org.bidib.jbidibc.message.FeatureResponse;
import org.bidib.jbidibc.message.FeatureSetMessage;
import org.bidib.jbidibc.message.FeedbackGetAddressRangeMessage;
import org.bidib.jbidibc.message.FeedbackGetConfidenceMessage;
import org.bidib.jbidibc.message.FeedbackGetRangeMessage;
import org.bidib.jbidibc.message.FeedbackMirrorFreeMessage;
import org.bidib.jbidibc.message.FeedbackMirrorMultipleMessage;
import org.bidib.jbidibc.message.FeedbackMirrorOccupiedMessage;
import org.bidib.jbidibc.message.FwUpdateOpMessage;
import org.bidib.jbidibc.message.FwUpdateStatResponse;
import org.bidib.jbidibc.message.NodeTabCountResponse;
import org.bidib.jbidibc.message.NodeTabGetAllMessage;
import org.bidib.jbidibc.message.NodeTabGetNextMessage;
import org.bidib.jbidibc.message.NodeTabResponse;
import org.bidib.jbidibc.message.SysDisableMessage;
import org.bidib.jbidibc.message.SysEnableMessage;
import org.bidib.jbidibc.message.SysGetPVersionMessage;
import org.bidib.jbidibc.message.SysGetSwVersionMessage;
import org.bidib.jbidibc.message.SysIdentifyMessage;
import org.bidib.jbidibc.message.SysIdentifyResponse;
import org.bidib.jbidibc.message.SysMagicMessage;
import org.bidib.jbidibc.message.SysMagicResponse;
import org.bidib.jbidibc.message.SysPVersionResponse;
import org.bidib.jbidibc.message.SysPingMessage;
import org.bidib.jbidibc.message.SysPongResponse;
import org.bidib.jbidibc.message.SysResetMessage;
import org.bidib.jbidibc.message.SysSwVersionResponse;
import org.bidib.jbidibc.message.SysUniqueIdMessage;
import org.bidib.jbidibc.message.SysUniqueIdResponse;
import org.bidib.jbidibc.message.VendorAckResponse;
import org.bidib.jbidibc.message.VendorDisableMessage;
import org.bidib.jbidibc.message.VendorEnableMessage;
import org.bidib.jbidibc.message.VendorGetMessage;
import org.bidib.jbidibc.message.VendorResponse;
import org.bidib.jbidibc.message.VendorSetMessage;
import org.bidib.jbidibc.node.listener.TransferListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BidibNode {
    private static final Logger LOGGER = LoggerFactory.getLogger(BidibNode.class);
    
    private static final Collection<TransferListener> listeners = new LinkedList<TransferListener>();

    private final byte[] addr;
    private final StringBuilder logRecord = new StringBuilder();
    private int nextReceiveMsgNum = 0;
    private Semaphore nextReceiveMsgNumSemaphore = new Semaphore(1);
    private int nextSendMsgNum = -1;
    private Semaphore nextSendMsgNumSemaphore = new Semaphore(1);
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    BidibNode(byte[] addr) {
        this.addr = addr;
    }

    public void acknowledgeFree(int detectorNumber) throws IOException, ProtocolException, InterruptedException {
        send(new FeedbackMirrorFreeMessage(detectorNumber), false);
    }

    public void acknowledgeMultiple(int baseAddress, int size, byte[] detectorData) throws IOException,
            ProtocolException, InterruptedException {
        send(new FeedbackMirrorMultipleMessage(baseAddress, size, detectorData), false);
    }

    public void acknowledgeOccupied(int detectorNumber) throws IOException, ProtocolException, InterruptedException {
        send(new FeedbackMirrorOccupiedMessage(detectorNumber), false);
    }

    public static void addTransferListener(TransferListener l) {
        listeners.add(l);
    }

    /**
     * Switch on track signal on the booster. 
     * @throws IOException
     * @throws ProtocolException
     * @throws InterruptedException
     */
    public void boosterOn() throws IOException, ProtocolException, InterruptedException {
        send(new BoostOnMessage(), false);
    }

    /**
     * Switch off track signal on the booster. 
     * @throws IOException
     * @throws ProtocolException
     * @throws InterruptedException
     */
    public void boosterOff() throws IOException, ProtocolException, InterruptedException {
        send(new BoostOffMessage(), false);
    }


    /**
     * Query the booster state. We don't wait for the response because the {@link MessageReceiver} fires the booster status callback on receipt. 
     * @throws IOException
     * @throws ProtocolException
     * @throws InterruptedException
     */
    public void boosterQuery() throws IOException, ProtocolException, InterruptedException {
        send(new BoostQueryMessage(), false);
    }
    
    private void escape(byte c) throws IOException {
        if ((c == (byte) BidibLibrary.BIDIB_PKT_MAGIC) || (c == (byte) BidibLibrary.BIDIB_PKT_ESCAPE)) {
            output.write((byte) BidibLibrary.BIDIB_PKT_ESCAPE);
            c = (byte) (c ^ 0x20);
        }
        output.write(c);
    }

    private static void fireReceiveStarted() {
        for (TransferListener l : listeners) {
            l.receiveStarted();
        }
    }

    private static void fireReceiveStopped() {
        for (TransferListener l : listeners) {
            l.receiveStopped();
        }
    }

    private static void fireSendStarted() {
        for (TransferListener l : listeners) {
            l.sendStarted();
        }
    }

    private static void fireSendStopped() {
        for (TransferListener l : listeners) {
            l.sendStopped();
        }
    }

    private void flush() throws IOException {
        byte[] bytes = output.toByteArray();

        Bidib.send(bytes);
        logRecord.append(" : ");
        for (int index = 0; index < bytes.length; index++) {
            logRecord.append(String.format("%02x ", bytes[index]));
        }
        LOGGER.debug("Flush logRecord: {}", logRecord);
        logRecord.setLength(0);
        output.reset();
    }

    protected byte[] getAddr() {
        return addr;
    }

    /**
     * Get the loco addresses in the specified range from the feedback system.
     * @param begin the start of Melderbits to be transfered 
     * @param end the end of Melderbits to be transfered
     */
    public void getAddressState(int begin, int end) throws IOException, ProtocolException, InterruptedException {
        send(new FeedbackGetAddressRangeMessage(begin, end), false);
    }
    /**
     * Get the current 'quality' of the track sensoring.
     */
    public void getConfidence() throws IOException, ProtocolException, InterruptedException {
        send(new FeedbackGetConfidenceMessage(), false);
    }

    public Feature getFeature(int number) throws IOException, ProtocolException, InterruptedException {
        Feature result = null;
        BidibMessage response = send(new FeatureGetMessage(number));

        if (response instanceof FeatureResponse) {
            result = ((FeatureResponse) response).getFeature();
        }
        return result;
    }

    public int getFeatureCount() throws IOException, ProtocolException, InterruptedException {
        return ((FeatureCountResponse) send(new FeatureGetAllMessage())).getCount();
    }

    /**
     * Get the status of the track sensoring in the specified range.
     * @param begin the start of Melderbits to be transfered 
     * @param end the end of Melderbits to be transfered
     */
    public void getFeedbackState(int begin, int end) throws IOException, ProtocolException, InterruptedException {
        send(new FeedbackGetRangeMessage(begin, end), false);
    }

    public int getMagic() throws IOException, ProtocolException, InterruptedException {
        return ((SysMagicResponse) send(new SysMagicMessage())).getMagic();
    }

    public Feature getNextFeature() throws IOException, ProtocolException, InterruptedException {
    	BidibMessage message = send(new FeatureGetNextMessage());
    	if (message instanceof FeatureResponse) {
    		return ((FeatureResponse) message).getFeature();
    	}
    	return null;
    }

    public Node getNextNode() throws IOException, ProtocolException, InterruptedException {
        Node result = null;
        BidibMessage response = send(new NodeTabGetNextMessage());

        if (response instanceof NodeTabResponse) {
            result = ((NodeTabResponse) response).getNode(addr);
        }
        return result;
    }

    public static int getNextReceiveMsgNum(BidibMessage message) {
        final BidibNode node = Bidib.getNode(new Node(0, message.getAddr(), 0));

        try {
            node.nextReceiveMsgNumSemaphore.acquire();
            node.nextReceiveMsgNum++;
            if (node.nextReceiveMsgNum > 255) {
                node.nextReceiveMsgNum = 1;
            }
            if (message.getNum() == 0) {
                node.nextReceiveMsgNum = 0;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            node.nextReceiveMsgNumSemaphore.release();
        }
        return node.nextReceiveMsgNum;
    }

    private byte getNextSendMsgNum() {
        try {
            nextSendMsgNumSemaphore.acquire();
            nextSendMsgNum++;
            if (nextSendMsgNum > 255) {
                nextSendMsgNum = 1;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            nextSendMsgNumSemaphore.release();
        }
        return (byte) nextSendMsgNum;
    }

    public int getNodeCount() throws IOException, ProtocolException, InterruptedException {
        return ((NodeTabCountResponse) send(new NodeTabGetAllMessage())).getCount();
    }

    public ProtocolVersion getPVersion() throws IOException, ProtocolException, InterruptedException {
        return ((SysPVersionResponse) send(new SysGetPVersionMessage())).getVersion();
    }

    public SoftwareVersion getSwVersion() throws IOException, ProtocolException, InterruptedException {
        return ((SysSwVersionResponse) send(new SysGetSwVersionMessage())).getVersion();
    }

    public byte[] getUniqueId() throws IOException, ProtocolException, InterruptedException {
        return ((SysUniqueIdResponse) send(new SysUniqueIdMessage())).getUniqueId();
    }

    public IdentifyState identify(IdentifyState state) throws IOException, ProtocolException, InterruptedException {
        return ((SysIdentifyResponse) send(new SysIdentifyMessage(state))).getState();
    }

    public boolean isUpdatable() throws IOException, ProtocolException, InterruptedException {
        Feature feature = getFeature(BidibLibrary.FEATURE_FW_UPDATE_MODE);

        return feature != null && feature.getValue() == 1;
    }

    public int ping() throws IOException, ProtocolException, InterruptedException {
        SysPongResponse result = (SysPongResponse) send(new SysPingMessage());

        return result.getNum();
    }

    private BidibMessage receive() throws IOException, ProtocolException, InterruptedException {
        BidibMessage result = null;
        LOGGER.debug("Receive message.");
        fireReceiveStarted();
        try {
            result = MessageReceiver.getMessage();
        } 
        finally {
            if (result == null) {
            	// no result was fetched
                resetNextSendMsgNum();
            }
        }
        fireReceiveStopped();
        return result;
    }

    public void reset() throws IOException, ProtocolException, InterruptedException {
        send(new SysResetMessage());
    }

    private void resetNextSendMsgNum() {
        try {
            nextSendMsgNumSemaphore.acquire();
            nextSendMsgNum = -1;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            nextSendMsgNumSemaphore.release();
        }
    }

    BidibMessage send(BidibMessage message) throws IOException, ProtocolException, InterruptedException {
        return send(message, true);
    }

    /**
     * Send a message and wait for answer if expectAnswer is true.
     * @param message the message to send
     * @param expectAnswer answer is expected, this will cause to wait for an answer
     * @return
     * @throws IOException
     * @throws ProtocolException
     * @throws InterruptedException
     */
    synchronized BidibMessage send(BidibMessage message, boolean expectAnswer) throws IOException, ProtocolException,
            InterruptedException {
        logRecord.append("send " + message + " to " + this);

        BidibMessage result = null;
        int num = getNextSendMsgNum();
        byte type = message.getType();
        byte[] data = message.getData();
        byte[] bytes = new byte[1 + (addr != null ? addr.length : 1) + 2 + (data != null ? data.length : 0)];
        int index = 0;

        bytes[index++] = (byte) (bytes.length - 1);
        if (addr != null) {
            for (int addrIndex = 0; addrIndex < addr.length; addrIndex++) {
                bytes[index++] = addr[addrIndex];
            }
        } else {
            bytes[index++] = 0;
        }
        bytes[index++] = (byte) num;
        bytes[index++] = type;
        if (data != null) {
            for (int dataIndex = 0; dataIndex < data.length; dataIndex++) {
                bytes[index++] = data[dataIndex];
            }
        }
        sendMessage(bytes);
        if (expectAnswer) {
            result = receive();
            if (result == null) {
                throw new ProtocolException("got no answer to " + message);
            }
        }
        return result;
    }

    private void sendDelimiter() throws IOException {
        output.write((byte) BidibLibrary.BIDIB_PKT_MAGIC);
    }

    public FirmwareUpdateStat sendFirmwareUpdateOperation(FirmwareUpdateOperation operation, byte... data)
            throws IOException, ProtocolException, InterruptedException {
        return ((FwUpdateStatResponse) send(new FwUpdateOpMessage(operation, data))).getUpdateStat();
    }

    private void sendMessage(byte[] message) throws IOException {
    	LOGGER.debug("Send the message: {}", message);
    	
        fireSendStarted();
        sendDelimiter();

        byte length = message[0];

        escape(length);

        int tx_crc = CRC8.getCrcValue(length);

        for (int i = 1; i <= length; i++) {
            escape(message[i]);
            tx_crc = CRC8.getCrcValue((message[i] ^ tx_crc) & 0xFF);
        }
        escape((byte) tx_crc);
        sendDelimiter();
        flush();
        fireSendStopped();
    }

    public void setFeature(int number, int value) throws IOException, ProtocolException, InterruptedException {
        send(new FeatureSetMessage(number, value));
    }

    public void sysDisable() throws IOException, ProtocolException, InterruptedException {
        send(new SysDisableMessage(), false);
    }

    public void sysEnable() throws IOException, ProtocolException, InterruptedException {
        send(new SysEnableMessage(), false);
    }

    public String toString() {
        return getClass().getSimpleName() + "@" + hashCode() + (addr != null ? Arrays.toString(addr) : "");
    }

    public boolean vendorDisable() throws IOException, ProtocolException, InterruptedException {
        return ((VendorAckResponse) send(new VendorDisableMessage())).getReturnCode() == 0;
    }

    public boolean vendorEnable(byte[] uniqueId) throws IOException, ProtocolException, InterruptedException {
        return ((VendorAckResponse) send(new VendorEnableMessage(uniqueId))).getReturnCode() == 1;
    }

    public VendorData vendorGet(String name) throws IOException, ProtocolException, InterruptedException {
        return ((VendorResponse) send(new VendorGetMessage(name))).getVendorData();
    }

    public VendorData vendorSet(String name, String value) throws IOException, ProtocolException, InterruptedException {
        return ((VendorResponse) send(new VendorSetMessage(name, value))).getVendorData();
    }
}
