package org.bidib.jbidibc.simulation.nodes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.StringUtils;
import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.Feature;
import org.bidib.jbidibc.VendorData;
import org.bidib.jbidibc.enumeration.FirmwareUpdateOperation;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.message.BidibCommand;
import org.bidib.jbidibc.message.FeatureCountResponse;
import org.bidib.jbidibc.message.FeatureGetMessage;
import org.bidib.jbidibc.message.FeatureNotAvailableResponse;
import org.bidib.jbidibc.message.FeatureResponse;
import org.bidib.jbidibc.message.FeatureSetMessage;
import org.bidib.jbidibc.message.FwUpdateOpMessage;
import org.bidib.jbidibc.message.FwUpdateStatResponse;
import org.bidib.jbidibc.message.NodeTabCountResponse;
import org.bidib.jbidibc.message.NodeTabResponse;
import org.bidib.jbidibc.message.StringGetMessage;
import org.bidib.jbidibc.message.StringResponse;
import org.bidib.jbidibc.message.StringSetMessage;
import org.bidib.jbidibc.message.SysMagicResponse;
import org.bidib.jbidibc.message.SysPVersionResponse;
import org.bidib.jbidibc.message.SysPingMessage;
import org.bidib.jbidibc.message.SysPongResponse;
import org.bidib.jbidibc.message.SysSwVersionResponse;
import org.bidib.jbidibc.message.VendorAckResponse;
import org.bidib.jbidibc.message.VendorEnableMessage;
import org.bidib.jbidibc.message.VendorGetMessage;
import org.bidib.jbidibc.message.VendorResponse;
import org.bidib.jbidibc.message.VendorSetMessage;
import org.bidib.jbidibc.simulation.SimulatorNode;
import org.bidib.jbidibc.simulation.events.SimulatorStatusEvent;
import org.bidib.jbidibc.simulation.net.SimulationBidibMessageProcessor;
import org.bidib.jbidibc.utils.ByteUtils;
import org.bushe.swing.event.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultNodeSimulator implements SimulatorNode {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultNodeSimulator.class);

    protected byte[] nodeAddress;

    protected long uniqueId;

    protected SortedMap<String, SimulatorNode> subNodes = new TreeMap<String, SimulatorNode>();

    private int sendNum;

    private String stringValue = "";

    private int currentFeature;

    private int featureCount;

    protected List<Feature> features = new LinkedList<>();

    protected Map<String, String> configurationVariables = new LinkedHashMap<String, String>();

    private BlockingQueue<BidibCommand> sendQueue = new LinkedBlockingQueue<BidibCommand>();

    private Thread requestWorker;

    protected final ScheduledExecutorService responseWorker = Executors.newScheduledThreadPool(1);

    private final SimulationBidibMessageProcessor messageReceiver;

    public DefaultNodeSimulator(byte[] nodeAddress, long uniqueId, SimulationBidibMessageProcessor messageReceiver) {
        this.nodeAddress = nodeAddress;
        this.messageReceiver = messageReceiver;
        this.uniqueId = uniqueId;

        LOGGER.info("Create default node simulator with address: {}, uniqueId: {}", nodeAddress, uniqueId);
    }

    protected byte[] getNodeAddress() {
        return nodeAddress;
    }

    protected void prepareFeatures() {
        features.add(new Feature(BidibLibrary.FEATURE_FW_UPDATE_MODE, 1));
        features.add(new Feature(BidibLibrary.FEATURE_STRING_SIZE, 24));
        features.add(new Feature(BidibLibrary.FEATURE_RELEVANT_PID_BITS, 16));
    }

    protected void prepareCVs() {
        // do nothing
    }

    @Override
    public Feature getFeature(int featureNum) {
        for (Feature feature : features) {
            if (feature.getType() == featureNum) {
                return feature;
            }
        }
        return null;
    }

    protected void prepared() {

        featureCount = features.size();
    }

    @Override
    public String getSimulationPanelClass() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getUniqueId() {
        return uniqueId;
    }

    @Override
    public String getAddress() {
        return ByteUtils.bytesToHex(nodeAddress);
    }

    private AtomicBoolean requestWorkerRunning = new AtomicBoolean();

    @Override
    public void start() {

        if (requestWorker != null) {
            LOGGER.error("The simulator with address {} is already started!", getAddress());
            throw new IllegalStateException("The simulator is already started!");
        }

        prepareFeatures();
        prepareCVs();
        prepared();

        // the address of myself is always 0
        String ownNodeAddress = "00";
        LOGGER.info("Add myself as subnode, address: {}, simulator: {}", ownNodeAddress, this);
        subNodes.put(ownNodeAddress.trim(), this);

        requestWorkerRunning.set(true);
        // use executor to send response
        requestWorker = new Thread(new Runnable() {
            @Override
            public void run() {
                LOGGER.info("Started request worker.");
                while (requestWorkerRunning.get()) {
                    try {
                        BidibCommand bidibMessage = sendQueue.take();
                        if (bidibMessage != null) {
                            LOGGER.info("Process message: {}", bidibMessage);
                            byte[] response = prepareResponse(bidibMessage);
                            publishResponse(response);

                            LOGGER.info("Process message has finished: {}", bidibMessage);
                        }
                        else {
                            LOGGER.info("No message available.");
                        }
                    }
                    catch (InterruptedException ex) {
                        if (requestWorkerRunning.get()) {
                            LOGGER.warn("Process request and send response failed.", ex);
                        }
                        else {
                            LOGGER.info("Request worker was interrupted while waiting for messages.");
                        }
                    }
                    catch (Exception ex) {
                        LOGGER.warn("Process request and send response failed.", ex);
                    }
                }
                LOGGER.info("Process message worker has finished.");
            }
        });
        requestWorker.start();

        SimulatorStatusEvent simulatorStatusEvent =
            new SimulatorStatusEvent(ByteUtils.bytesToHex(nodeAddress), SimulatorStatusEvent.Status.started);
        EventBus.publish(simulatorStatusEvent);

    }

    @Override
    public void stop() {
        LOGGER.info("The simulator is stopped.");
        // for (SimulatorListener listener : simulatorListeners) {
        // listener.stop();
        // }

        SimulatorStatusEvent simulatorStatusEvent =
            new SimulatorStatusEvent(ByteUtils.bytesToHex(nodeAddress), SimulatorStatusEvent.Status.stopped);
        EventBus.publish(simulatorStatusEvent);

        responseWorker.shutdownNow();

        if (requestWorker != null) {
            try {
                requestWorkerRunning.set(false);
                requestWorker.interrupt();
                requestWorker.join(2000);
            }
            catch (InterruptedException e) {
                LOGGER.warn("Interrupt and wait for request worker failed.", e);
            }
            requestWorker = null;
        }
    }

    protected byte[] prepareResponse(BidibCommand bidibMessage) {
        byte[] response = null;
        switch (ByteUtils.getInt(bidibMessage.getType())) {
            case BidibLibrary.MSG_SYS_GET_MAGIC:
                response = processSysGetMagicRequest(bidibMessage);
                break;
            case BidibLibrary.MSG_SYS_CLOCK:
                processSysClockRequest(bidibMessage);
                break;
            case BidibLibrary.MSG_SYS_GET_P_VERSION:
                response = processSysGetPVersionRequest(bidibMessage);
                break;
            case BidibLibrary.MSG_SYS_GET_SW_VERSION:
                response = processSysGetSwVersionRequest(bidibMessage);
                break;
            case BidibLibrary.MSG_SYS_ENABLE:
                processSysEnableRequest(bidibMessage);
                break;
            case BidibLibrary.MSG_SYS_DISABLE:
                processSysDisableRequest(bidibMessage);
                break;
            case BidibLibrary.MSG_SYS_IDENTIFY:
                processSysIdentifyRequest(bidibMessage);
                break;
            case BidibLibrary.MSG_SYS_PING:
                processSysPingRequest(bidibMessage);
                break;
            case BidibLibrary.MSG_NODETAB_GETALL:
                response = processNodeTabGetAllRequest(bidibMessage);
                break;
            case BidibLibrary.MSG_NODETAB_GETNEXT:
                response = processNodeTabGetNextRequest(bidibMessage);
                break;
            case BidibLibrary.MSG_FEATURE_GET:
                response = processFeatureGetRequest(bidibMessage);
                break;
            case BidibLibrary.MSG_FEATURE_SET:
                response = processFeatureSetRequest(bidibMessage);
                break;
            case BidibLibrary.MSG_FEATURE_GETALL:
                response = processFeatureGetAllRequest(bidibMessage);
                break;
            case BidibLibrary.MSG_FEATURE_GETNEXT:
                response = processFeatureGetNextRequest(bidibMessage);
                break;
            case BidibLibrary.MSG_VENDOR_ENABLE:
                response = processVendorEnableRequest(bidibMessage);
                break;
            case BidibLibrary.MSG_VENDOR_DISABLE:
                response = processVendorDisableRequest(bidibMessage);
                break;
            case BidibLibrary.MSG_VENDOR_SET:
                response = processVendorSetRequest(bidibMessage);
                break;
            case BidibLibrary.MSG_VENDOR_GET:
                response = processVendorGetRequest(bidibMessage);
                break;
            case BidibLibrary.MSG_STRING_SET:
                response = processStringSetRequest(bidibMessage);
                break;
            case BidibLibrary.MSG_STRING_GET:
                response = processStringGetRequest(bidibMessage);
                break;
            case BidibLibrary.MSG_SYS_RESET:
                LOGGER.info("MSG_SYS_RESET, reset sendNum.");
                resetSendNum();
                break;
            case BidibLibrary.MSG_FW_UPDATE_OP:
                response = processFwUpdateOpRequest(bidibMessage);
                break;
            default:
                LOGGER.warn("Unknown message detected: {}", bidibMessage);
                break;
        }
        return response;
    }

    @Override
    public void processRequest(final BidibCommand bidibCommand) {
        LOGGER.info("Add request to send queue: {}", bidibCommand);

        sendQueue.offer(bidibCommand);
        LOGGER.info("Scheduled request for processing: {}", bidibCommand);
    }

    /**
     * This method is only called by the single worker thread.
     * 
     * @param response
     *            the response to send
     */
    protected synchronized void publishResponse(final byte[] response) {
        LOGGER.debug("Publish response.");
        if (response != null) {
            try {
                // set the send message number
                int sendMsgNum = getNextResponseSendNum();
                int index = 1;
                while (response[index] != 0) {
                    index++;
                }
                response[index + 1] = ByteUtils.getLowByte(sendMsgNum);

                ByteArrayOutputStream output = new ByteArrayOutputStream();
                output.write(response);
                output.flush();
                LOGGER.info("Send output with sendMsgNum: {} to receiver: {}, content: {}", sendMsgNum,
                    messageReceiver, ByteUtils.bytesToHex(response));

                // process the messages
                messageReceiver.publishResponse(output);
            }
            catch (ProtocolException | IOException ex) {
                LOGGER.warn("Send message to publish in messageReceiver failed.", ex);
            }
            catch (Exception ex) {
                LOGGER.warn("Publish messages failed.", ex);
            }
        }
        else {
            LOGGER.info("No response available to send.");
        }
    }

    protected void sendSpontanousResponse(final byte[] response) {
        LOGGER.info("Send spontanous response: {}", response);

        // use executor to send response
        responseWorker.schedule(new Runnable() {
            @Override
            public void run() {
                LOGGER.info("Send response: {}", response);
                publishResponse(response);
                LOGGER.info("Send response worker has finished.");
            }
        }, 0, TimeUnit.MILLISECONDS);
    }

    protected byte[] processSysGetMagicRequest(BidibCommand bidibMessage) {

        byte[] response = null;
        try {
            SysMagicResponse magicResponse =
                new SysMagicResponse(bidibMessage.getAddr(), getNextSendNum(), (byte) 0xFE, (byte) 0xAF);
            response = magicResponse.getContent();
        }
        catch (ProtocolException ex) {
            LOGGER.warn("Create magic response failed.", ex);
        }
        return response;
    }

    protected void processSysClockRequest(BidibCommand bidibMessage) {
        LOGGER.info("Process the SysClockMessage: {}, do nothing ...", bidibMessage);
    }

    protected byte[] processSysGetPVersionRequest(BidibCommand bidibMessage) {
        LOGGER.info("Process the SysGetPVersion request: {}, do nothing ...", bidibMessage);
        byte[] response = null;
        try {
            SysPVersionResponse sysPVersionResponse =
                new SysPVersionResponse(bidibMessage.getAddr(), getNextSendNum(), (byte) 0x01, (byte) 0x02);
            response = sysPVersionResponse.getContent();
        }
        catch (ProtocolException ex) {
            LOGGER.warn("Create sysPVersion response failed.", ex);
        }
        return response;
    }

    protected byte[] processSysGetSwVersionRequest(BidibCommand bidibMessage) {
        LOGGER.info("Process the SysGetSwVersion request: {}", bidibMessage);
        byte[] response = null;
        try {
            SysSwVersionResponse sysSwVersionResponse =
                new SysSwVersionResponse(bidibMessage.getAddr(), getNextSendNum(), (byte) 0x01, (byte) 0x02,
                    (byte) 0x03);
            response = sysSwVersionResponse.getContent();
        }
        catch (ProtocolException ex) {
            LOGGER.warn("Create sysSwVersion response failed.", ex);
        }
        return response;
    }

    protected void processSysEnableRequest(BidibCommand bidibMessage) {
        LOGGER.info("Process the SysEnable request: {}, do nothing ...", bidibMessage);
    }

    protected void processSysDisableRequest(BidibCommand bidibMessage) {
        LOGGER.info("Process the SysDisable request: {}, do nothing ...", bidibMessage);
    }

    protected void processSysIdentifyRequest(BidibCommand bidibMessage) {
        // TODO Process the SysIdentify request
        LOGGER.info("Process the SysIdentify request: {}", bidibMessage);
    }

    protected byte[] processSysPingRequest(BidibCommand bidibMessage) {
        LOGGER.info("Process the SysPing request: {}", bidibMessage);

        byte[] response = null;
        try {
            SysPingMessage sysPingMessage = (SysPingMessage) bidibMessage;
            SysPongResponse sysPongResponse =
                new SysPongResponse(bidibMessage.getAddr(), getNextSendNum(), sysPingMessage.getMarker());
            response = sysPongResponse.getContent();
        }
        catch (ProtocolException ex) {
            LOGGER.warn("Create sysPong response failed.", ex);
        }
        return response;
    }

    protected byte[] processNodeTabGetAllRequest(BidibCommand bidibMessage) {

        byte[] response = null;
        try {
            NodeTabCountResponse nodeTabCountResponse =
                new NodeTabCountResponse(bidibMessage.getAddr(), getNextSendNum(), (byte) 0x01);
            response = nodeTabCountResponse.getContent();
        }
        catch (ProtocolException ex) {
            LOGGER.warn("Create nodeTabCount response failed.", ex);
        }
        return response;
    }

    protected byte[] processNodeTabGetNextRequest(BidibCommand bidibMessage) {
        // 01.01.2014 20:35:29.917: receive NodeTabResponse[[0],num=2,type=137,data=[1, 0, 210, 0, 13, 104, 0, 0, 54]] :
        // 0C 00 02 89 01 00 D2 00 0D 68 00 00 36

        byte[] response = null;
        byte nodeTabVersion = 1;
        byte localAddr = 0;
        try {
            NodeTabResponse nodeTabResponse =
                new NodeTabResponse(bidibMessage.getAddr(), getNextSendNum(), nodeTabVersion, localAddr, uniqueId);
            response = nodeTabResponse.getContent();
            LOGGER.info("Prepared nodeTab response: {}", ByteUtils.bytesToHex(response));
        }
        catch (ProtocolException ex) {
            LOGGER.warn("Create nodeTab response failed.", ex);
        }
        return response;
    }

    protected byte[] processFeatureGetRequest(BidibCommand bidibMessage) {

        byte[] response = null;
        try {
            FeatureGetMessage featureGetMessage = (FeatureGetMessage) bidibMessage;
            int featureNum = featureGetMessage.getNumber();
            LOGGER.info("Get feature with number: {}", featureNum);

            Feature foundFeature = null;

            for (Feature feature : features) {
                if (feature.getType() == featureNum) {
                    foundFeature = feature;
                    LOGGER.info("Found feature: {}", foundFeature);
                    break;
                }
            }

            if (foundFeature != null) {
                FeatureResponse featureResponse =
                    new FeatureResponse(featureGetMessage.getAddr(), getNextSendNum(), featureNum,
                        foundFeature.getValue());
                response = featureResponse.getContent();
                LOGGER.info("Prepared response: {}", ByteUtils.bytesToHex(response));
            }
            else {
                FeatureNotAvailableResponse featureResponse =
                    new FeatureNotAvailableResponse(bidibMessage.getAddr(), getNextSendNum(), featureNum);
                response = featureResponse.getContent();
            }
        }
        catch (ProtocolException ex) {
            LOGGER.warn("Create feature response failed.", ex);
        }
        return response;
    }

    protected byte[] processFeatureSetRequest(BidibCommand bidibMessage) {

        byte[] response = null;
        try {
            FeatureSetMessage featureSetMessage = (FeatureSetMessage) bidibMessage;
            int featureNum = featureSetMessage.getNumber();
            int featureValue = featureSetMessage.getValue();
            LOGGER.info("Set feature with number: {}, value: {}", featureNum, featureValue);

            Feature foundFeature = null;

            for (Feature feature : features) {
                if (feature.getType() == featureNum) {
                    foundFeature = feature;
                    foundFeature.setValue(featureValue);
                    LOGGER.info("Found feature: {}", foundFeature);
                    break;
                }
            }

            if (foundFeature != null) {
                FeatureResponse featureResponse =
                    new FeatureResponse(featureSetMessage.getAddr(), getNextSendNum(), featureNum,
                        foundFeature.getValue());
                response = featureResponse.getContent();
                LOGGER.info("Prepared response: {}", ByteUtils.bytesToHex(response));
            }
            else {
                FeatureNotAvailableResponse featureResponse =
                    new FeatureNotAvailableResponse(bidibMessage.getAddr(), getNextSendNum(), featureNum);
                response = featureResponse.getContent();
            }
        }
        catch (ProtocolException ex) {
            LOGGER.warn("Create feature response failed.", ex);
        }
        return response;
    }

    protected byte[] processFeatureGetAllRequest(BidibCommand bidibMessage) {

        byte[] response = null;
        try {
            FeatureCountResponse featureResponse =
                new FeatureCountResponse(bidibMessage.getAddr(), getNextSendNum(), featureCount);
            response = featureResponse.getContent();
        }
        catch (ProtocolException ex) {
            LOGGER.warn("Create feature count response failed.", ex);
        }
        currentFeature = 0;

        return response;
    }

    protected byte[] processFeatureGetNextRequest(BidibCommand bidibMessage) {

        byte[] response = null;
        if (currentFeature >= featureCount) {
            try {
                FeatureNotAvailableResponse featureResponse =
                    new FeatureNotAvailableResponse(bidibMessage.getAddr(), getNextSendNum(), 255);
                response = featureResponse.getContent();
            }
            catch (ProtocolException ex) {
                LOGGER.warn("Create feature N/A response failed.", ex);
            }
        }
        else {
            try {
                Feature feature = features.get(currentFeature);
                FeatureResponse featureResponse =
                    new FeatureResponse(bidibMessage.getAddr(), getNextSendNum(), feature.getType(), feature.getValue());
                response = featureResponse.getContent();
            }
            catch (ProtocolException ex) {
                LOGGER.warn("Create feature response failed.", ex);
            }
            catch (Exception ex) {
                LOGGER.warn("Create feature response failed.", ex);
            }
        }
        currentFeature++;
        return response;
    }

    protected byte[] processVendorEnableRequest(BidibCommand bidibMessage) {

        byte[] response = null;
        try {
            VendorEnableMessage vendorEnableMessage = (VendorEnableMessage) bidibMessage;
            long uniqueId = vendorEnableMessage.getUniqueId();
            LOGGER.info("Enable the user config mode for uniqueId: {}", uniqueId);
            byte userConfigModeActive = 1;
            VendorAckResponse vendorAckResponse =
                new VendorAckResponse(bidibMessage.getAddr(), getNextSendNum(), userConfigModeActive);
            response = vendorAckResponse.getContent();
        }
        catch (ProtocolException ex) {
            LOGGER.warn("Create vendor ack response failed.", ex);
        }
        return response;
    }

    protected byte[] processVendorDisableRequest(BidibCommand bidibMessage) {

        byte[] response = null;
        try {
            // VendorDisableMessage vendorDisableMessage = (VendorDisableMessage) bidibMessage;
            LOGGER.info("Disable the user config mode");
            byte userConfigModeActive = 0;
            VendorAckResponse vendorAckResponse =
                new VendorAckResponse(bidibMessage.getAddr(), getNextSendNum(), userConfigModeActive);
            response = vendorAckResponse.getContent();
        }
        catch (ProtocolException ex) {
            LOGGER.warn("Create vendor ack response failed.", ex);
        }
        return response;
    }

    protected byte[] processVendorSetRequest(BidibCommand bidibMessage) {

        byte[] response = null;
        try {
            VendorSetMessage vendorSetMessage = (VendorSetMessage) bidibMessage;
            VendorData vendorData = vendorSetMessage.getVendorData();
            LOGGER.info("Set the vendor data: {}", vendorData);

            // TODO store the vendor data ...
            configurationVariables.put(vendorData.getName(), vendorData.getValue());

            VendorResponse vendorResponse =
                new VendorResponse(bidibMessage.getAddr(), getNextSendNum(), vendorData.getName(),
                    vendorData.getValue());
            response = vendorResponse.getContent();
        }
        catch (ProtocolException ex) {
            LOGGER.warn("Create vendor response failed.", ex);
        }
        return response;
    }

    protected byte[] processVendorGetRequest(BidibCommand bidibMessage) {

        byte[] response = null;
        try {
            VendorGetMessage vendorGetMessage = (VendorGetMessage) bidibMessage;
            String vendorDataName = vendorGetMessage.getName();
            LOGGER.info("Get the vendor data with name: {}", vendorDataName);

            // TODO fetch the value from the stored data ...
            String vendorDataValue = configurationVariables.get(vendorDataName);
            if (StringUtils.isBlank(vendorDataValue)) {
                vendorDataValue = "";
            }

            VendorResponse vendorResponse =
                new VendorResponse(bidibMessage.getAddr(), getNextSendNum(), vendorDataName, vendorDataValue);
            response = vendorResponse.getContent();
        }
        catch (ProtocolException ex) {
            LOGGER.warn("Create vendor response failed.", ex);
        }
        return response;
    }

    protected byte[] processStringSetRequest(BidibCommand bidibMessage) {

        byte[] response = null;
        try {
            StringSetMessage stringSetMessage = (StringSetMessage) bidibMessage;
            stringValue = stringSetMessage.getString();
            StringResponse stringResponse =
                new StringResponse(bidibMessage.getAddr(), getNextSendNum(), ByteUtils.getLowByte(stringSetMessage
                    .getNamespace()), ByteUtils.getLowByte(stringSetMessage.getStringId()), stringValue);
            response = stringResponse.getContent();
        }
        catch (ProtocolException ex) {
            LOGGER.warn("Create string response failed.", ex);
        }
        return response;
    }

    protected byte[] processStringGetRequest(BidibCommand bidibMessage) {

        byte[] response = null;
        try {
            StringGetMessage stringGetMessage = (StringGetMessage) bidibMessage;
            StringResponse stringResponse =
                new StringResponse(bidibMessage.getAddr(), getNextSendNum(), ByteUtils.getLowByte(stringGetMessage
                    .getNamespace()), ByteUtils.getLowByte(stringGetMessage.getStringId()), stringValue);
            response = stringResponse.getContent();
        }
        catch (ProtocolException ex) {
            LOGGER.warn("Create string response failed.", ex);
        }
        return response;
    }

    protected byte[] processFwUpdateOpRequest(BidibCommand bidibMessage) {

        byte[] response = null;
        try {
            FwUpdateOpMessage fwUpdateOpMessage = (FwUpdateOpMessage) bidibMessage;
            FirmwareUpdateOperation operation = fwUpdateOpMessage.getOperation();

            LOGGER.info("processFwUpdateOpRequest, operation: {}", operation);

            int timeout = 0;
            int status = BidibLibrary.BIDIB_MSG_FW_UPDATE_STAT_ERROR;
            switch (operation) {
                case ENTER:
                    status = BidibLibrary.BIDIB_MSG_FW_UPDATE_STAT_READY;
                    break;
                case DATA:
                    status = BidibLibrary.BIDIB_MSG_FW_UPDATE_STAT_DATA;
                    break;
                case SETDEST:
                    status = BidibLibrary.BIDIB_MSG_FW_UPDATE_STAT_DATA;
                    break;
                case EXIT:
                    status = BidibLibrary.BIDIB_MSG_FW_UPDATE_STAT_EXIT;
                    break;
                default:
                    break;
            }
            FwUpdateStatResponse fwUpdateStatResponse =
                new FwUpdateStatResponse(bidibMessage.getAddr(), getNextSendNum(), ByteUtils.getLowByte(status),
                    ByteUtils.getLowByte(timeout));
            response = fwUpdateStatResponse.getContent();
        }
        catch (ProtocolException ex) {
            LOGGER.warn("Create fwUpdateStat response failed.", ex);
        }
        return response;
    }

    protected void resetSendNum() {
        sendNum = 0;
    }

    protected int getNextSendNum() {
        // int nextSendNum = sendNum;
        // sendNum++;
        // if (sendNum > 255) {
        // sendNum = 0;
        // }
        // return nextSendNum;
        return 0;
    }

    protected int getNextResponseSendNum() {
        int nextSendNum = sendNum;
        sendNum++;
        if (sendNum > 255) {
            sendNum = 0;
        }
        return nextSendNum;
    }

    /**
     * @param address
     *            the address
     * @return <code>true</code>: if the nodeAddress equals the provided address, <code>false</code>: otherwise
     */
    protected boolean isAddressEqual(String address) {
        if (!ByteUtils.bytesToHex(this.nodeAddress).equals(address)) {
            LOGGER.trace("Another node is addressed.");
            return false;
        }
        return true;
    }

    @Override
    public void queryStatus(Class<?> portClass) {
    }
}
