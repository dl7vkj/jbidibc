package org.bidib.jbidibc.core.message;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.enumeration.ActivateCoilEnum;
import org.bidib.jbidibc.core.enumeration.AddressTypeEnum;
import org.bidib.jbidibc.core.enumeration.LcOutputType;
import org.bidib.jbidibc.core.enumeration.TimeBaseUnitEnum;
import org.bidib.jbidibc.core.enumeration.TimingControlEnum;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.schema.BidibFactory;
import org.bidib.jbidibc.core.schema.bidib.MessageType;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestFactory.class);

    private static final Logger MSG_RAW_LOGGER = LoggerFactory.getLogger("RAW");

    private Map<Integer, MessageType> messageTypeMap;

    public RequestFactory() {
    }

    private synchronized Map<Integer, MessageType> getMessageTypeMap() {
        if (messageTypeMap == null) {
            messageTypeMap = new LinkedHashMap<Integer, MessageType>();

            List<MessageType> messageTypes = BidibFactory.getMessageTypes();
            for (MessageType messageType : messageTypes) {
                messageTypeMap.put(Integer.valueOf(messageType.getId()), messageType);
            }
        }
        return messageTypeMap;
    }

    /**
     * Create messages from the provided byte array.
     * 
     * @param messageData
     *            the byte array with the messages
     * @return the list of BidibCommands
     * @throws ProtocolException
     */
    public List<BidibCommand> create(byte[] messageData) throws ProtocolException {
        LOGGER.debug("Create bidib message from raw messageData: {}", messageData);

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        List<BidibCommand> bidibMessages = new ArrayList<BidibCommand>();

        boolean escapeHot = false;
        StringBuilder logRecord = new StringBuilder();

        // read the values from in the port
        for (byte data : messageData) {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("received data: {}", ByteUtils.byteToHex(data));
            }
            // append data to log record
            logRecord.append(ByteUtils.byteToHex(data)).append(" ");

            // check if the current is the end of a packet
            if (data == ByteUtils.getLowByte(BidibLibrary.BIDIB_PKT_MAGIC) && output.size() > 0) {

                LOGGER.debug("Received raw message: {}", logRecord);
                if (MSG_RAW_LOGGER.isInfoEnabled()) {
                    MSG_RAW_LOGGER.info("<< {}", logRecord);
                }
                logRecord.setLength(0);

                // the message is complete
                byte[] messageContent = output.toByteArray();
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("prepared user data: {}, total len: {}", ByteUtils.bytesToHex(messageContent),
                        messageContent.length);
                }
                // read the first byte that is the length of the message
                byte len = messageContent[0];
                if ((len + 1) < messageContent.length - 1) {
                    // we have multiple messages
                    int index = 0;
                    while (index < messageContent.length - 1) {
                        len = messageContent[index];
                        byte[] part = new byte[len + 1];
                        System.arraycopy(messageContent, index, part, 0, len + 1);
                        BidibCommand bidibMessage = createConcreteMessage(part);
                        bidibMessages.add(bidibMessage);
                        LOGGER.trace("Added new bidibMessage: {}, index: {}", bidibMessage, index);
                        index += len + 1;
                    }
                }
                else {
                    // we have a single messages
                    BidibCommand bidibMessage = createConcreteMessage(messageContent);
                    bidibMessages.add(bidibMessage);
                    LOGGER.trace("Added new bidibMessage: {}", bidibMessage);
                }

                output.reset();
                break;
            }
            else {
                if (data == ByteUtils.getLowByte(BidibLibrary.BIDIB_PKT_ESCAPE)) {
                    escapeHot = true;
                }
                else if (data != ByteUtils.getLowByte(BidibLibrary.BIDIB_PKT_MAGIC)) {
                    if (escapeHot) {
                        data ^= 0x20;
                        escapeHot = false;
                    }
                    output.write((byte) data);
                }
            }
        }
        return bidibMessages;
    }

    private BidibCommand createConcreteMessage(byte[] message) throws ProtocolException {

        // TODO find a better way to get the message type without creating a BidibMessage instance first
        BidibMessage bidibMessage = new BidibMessage(message);

        BidibCommand concreteBidibMessage = null;
        int type = ByteUtils.getInt(bidibMessage.getType());
        switch (type) {
            case BidibLibrary.MSG_SYS_RESET:
                concreteBidibMessage = new SysResetMessage(message);
                break;
            case BidibLibrary.MSG_SYS_GET_MAGIC:
                concreteBidibMessage = new SysMagicMessage(message);
                break;
            case BidibLibrary.MSG_SYS_CLOCK:
                concreteBidibMessage = new SysClockMessage(message);
                break;
            case BidibLibrary.MSG_SYS_GET_P_VERSION:
                concreteBidibMessage = new SysGetPVersionMessage(message);
                break;
            case BidibLibrary.MSG_SYS_GET_SW_VERSION:
                concreteBidibMessage = new SysGetSwVersionMessage(message);
                break;
            case BidibLibrary.MSG_SYS_ENABLE:
                concreteBidibMessage = new SysEnableMessage(message);
                break;
            case BidibLibrary.MSG_SYS_DISABLE:
                concreteBidibMessage = new SysDisableMessage(message);
                break;
            case BidibLibrary.MSG_SYS_IDENTIFY:
                concreteBidibMessage = new SysIdentifyMessage(message);
                break;
            case BidibLibrary.MSG_SYS_PING:
                concreteBidibMessage = new SysPingMessage(message);
                break;
            case BidibLibrary.MSG_NODETAB_GETALL:
                concreteBidibMessage = new NodeTabGetAllMessage(message);
                break;
            case BidibLibrary.MSG_NODETAB_GETNEXT:
                concreteBidibMessage = new NodeTabGetNextMessage(message);
                break;
            case BidibLibrary.MSG_NODE_CHANGED_ACK:
                concreteBidibMessage = new NodeChangedAckMessage(message);
                break;
            case BidibLibrary.MSG_SYS_GET_ERROR:
                concreteBidibMessage = new SysGetErrorMessage(message);
                break;
            case BidibLibrary.MSG_FW_UPDATE_OP:
                concreteBidibMessage = new FwUpdateOpMessage(message);
                break;
            case BidibLibrary.MSG_CS_SET_STATE:
                concreteBidibMessage = new CommandStationSetStateMessage(message);
                break;
            case BidibLibrary.MSG_CS_DRIVE:
                concreteBidibMessage = new CommandStationDriveMessage(message);
                break;
            case BidibLibrary.MSG_CS_ACCESSORY:
                concreteBidibMessage = new CommandStationAccessoryMessage(message);
                break;
            case BidibLibrary.MSG_CS_POM:
                concreteBidibMessage = new CommandStationPomMessage(message);
                break;
            case BidibLibrary.MSG_CS_PROG:
                concreteBidibMessage = new CommandStationProgMessage(message);
                break;
            case BidibLibrary.MSG_BOOST_QUERY:
                concreteBidibMessage = new BoostQueryMessage(message);
                break;
            case BidibLibrary.MSG_BOOST_ON:
                concreteBidibMessage = new BoostOnMessage(message);
                break;
            case BidibLibrary.MSG_BOOST_OFF:
                concreteBidibMessage = new BoostOffMessage(message);
                break;
            case BidibLibrary.MSG_FEATURE_GET:
                concreteBidibMessage = new FeatureGetMessage(message);
                break;
            case BidibLibrary.MSG_FEATURE_SET:
                concreteBidibMessage = new FeatureSetMessage(message);
                break;
            case BidibLibrary.MSG_FEATURE_GETNEXT:
                concreteBidibMessage = new FeatureGetNextMessage(message);
                break;
            case BidibLibrary.MSG_FEATURE_GETALL:
                concreteBidibMessage = new FeatureGetAllMessage(message);
                break;
            case BidibLibrary.MSG_VENDOR_ENABLE:
                concreteBidibMessage = new VendorEnableMessage(message);
                break;
            case BidibLibrary.MSG_VENDOR_DISABLE:
                concreteBidibMessage = new VendorDisableMessage(message);
                break;
            case BidibLibrary.MSG_VENDOR_SET:
                concreteBidibMessage = new VendorSetMessage(message);
                break;
            case BidibLibrary.MSG_VENDOR_GET:
                concreteBidibMessage = new VendorGetMessage(message);
                break;
            case BidibLibrary.MSG_STRING_GET:
                concreteBidibMessage = new StringGetMessage(message);
                break;
            case BidibLibrary.MSG_STRING_SET:
                concreteBidibMessage = new StringSetMessage(message);
                break;
            case BidibLibrary.MSG_LC_OUTPUT:
                concreteBidibMessage = new LcOutputMessage(message);
                break;
            case BidibLibrary.MSG_LC_CONFIG_SET:
                concreteBidibMessage = new LcConfigSetMessage(message);
                break;
            case BidibLibrary.MSG_LC_CONFIG_GET:
                concreteBidibMessage = new LcConfigGetMessage(message);
                break;
            case BidibLibrary.MSG_LC_CONFIGX_SET:
                concreteBidibMessage = new LcConfigXSetMessage(message);
                break;
            case BidibLibrary.MSG_LC_CONFIGX_GET:
                concreteBidibMessage = new LcConfigXGetMessage(message);
                break;
            case BidibLibrary.MSG_LC_KEY_QUERY:
                concreteBidibMessage = new LcKeyMessage(message);
                break;
            case BidibLibrary.MSG_LC_OUTPUT_QUERY:
                concreteBidibMessage = new LcOutputQueryMessage(message);
                break;
            case BidibLibrary.MSG_LC_MAPPING_CFG:
                concreteBidibMessage = new LcMappingCfgMessage(message);
                break;
            case BidibLibrary.MSG_LC_MACRO_PARA_GET:
                concreteBidibMessage = new LcMacroParaGetMessage(message);
                break;
            case BidibLibrary.MSG_LC_MACRO_PARA_SET:
                concreteBidibMessage = new LcMacroParaSetMessage(message);
                break;
            case BidibLibrary.MSG_LC_MACRO_GET:
                concreteBidibMessage = new LcMacroGetMessage(message);
                break;
            case BidibLibrary.MSG_LC_MACRO_SET:
                concreteBidibMessage = new LcMacroSetMessage(message);
                break;
            case BidibLibrary.MSG_LC_MACRO_HANDLE:
                concreteBidibMessage = new LcMacroHandleMessage(message);
                break;
            case BidibLibrary.MSG_ACCESSORY_GET:
                concreteBidibMessage = new AccessoryGetMessage(message);
                break;
            case BidibLibrary.MSG_ACCESSORY_SET:
                concreteBidibMessage = new AccessorySetMessage(message);
                break;
            case BidibLibrary.MSG_ACCESSORY_PARA_GET:
                concreteBidibMessage = new AccessoryParaGetMessage(message);
                break;
            case BidibLibrary.MSG_ACCESSORY_PARA_SET:
                concreteBidibMessage = new AccessoryParaSetMessage(message);
                break;
            case BidibLibrary.MSG_BM_GET_RANGE:
                concreteBidibMessage = new FeedbackGetRangeMessage(message);
                break;
            case BidibLibrary.MSG_BM_ADDR_GET_RANGE:
                concreteBidibMessage = new FeedbackGetAddressRangeMessage(message);
                break;
            case BidibLibrary.MSG_BM_GET_CONFIDENCE:
                concreteBidibMessage = new FeedbackGetConfidenceMessage(message);
                break;

            default:
                concreteBidibMessage = new UnknownCommandMessage(message);
                break;
        }

        MessageType mt = getMessageTypeMap().get(type);
        if (mt != null) {
            concreteBidibMessage.setAnswerSize(mt.getAnswerSize());
        }
        else {
            LOGGER.warn("Unknown message detected, cannot set answer size: {}", concreteBidibMessage);
        }

        return concreteBidibMessage;
    }

    private BidibCommand initializeCommand(BidibCommand bidibCommand) {

        MessageType mt = getMessageTypeMap().get(Integer.valueOf(ByteUtils.getInt(bidibCommand.getType())));
        if (mt != null) {
            bidibCommand.setAnswerSize(mt.getAnswerSize());
        }
        else {
            LOGGER.warn("Unknown command detected, cannot set answer size: {}", bidibCommand);
        }

        return bidibCommand;
    }

    public BidibCommand createVendorGet(String name) {
        BidibCommand bidibCommand = new VendorGetMessage(name);
        bidibCommand = initializeCommand(bidibCommand);
        return bidibCommand;
    }

    public BidibCommand createFeatureSet(int number, int value) {
        BidibCommand bidibCommand = new FeatureSetMessage(number, value);
        return initializeCommand(bidibCommand);
    }

    public BidibCommand createFeatureGet(int number) {
        BidibCommand bidibCommand = new FeatureGetMessage(number);
        return initializeCommand(bidibCommand);
    }

    public BidibCommand createFeatureGetNext() {
        BidibCommand bidibCommand = new FeatureGetNextMessage();
        return initializeCommand(bidibCommand);
    }

    public BidibCommand createFeatureGetAll() {
        BidibCommand bidibCommand = new FeatureGetAllMessage();
        return initializeCommand(bidibCommand);
    }

    public BidibCommand createSysDisable() {
        BidibCommand bidibCommand = new SysDisableMessage();
        return initializeCommand(bidibCommand);
    }

    public BidibCommand createSysEnable() {
        BidibCommand bidibCommand = new SysEnableMessage();
        return initializeCommand(bidibCommand);
    }

    public BidibCommand createSysMagic() {
        BidibCommand bidibCommand = new SysMagicMessage();
        return initializeCommand(bidibCommand);
    }

    public BidibCommand createNodeTabGetAll() {
        BidibCommand bidibCommand = new NodeTabGetAllMessage();
        return initializeCommand(bidibCommand);
    }

    public BidibCommand createNodeTabGetNext() {
        BidibCommand bidibCommand = new NodeTabGetNextMessage();
        return initializeCommand(bidibCommand);
    }

    public BidibCommand createLcMacroParaGet(int macroNumber, int parameter) {
        BidibCommand bidibCommand = new LcMacroParaGetMessage(macroNumber, parameter);
        return initializeCommand(bidibCommand);
    }

    public BidibCommand createLcConfigGet(LcOutputType outputType, int outputNumber) {
        BidibCommand bidibCommand = new LcConfigGetMessage(outputType, outputNumber);
        return initializeCommand(bidibCommand);
    }

    public BidibCommand createLcConfigXGet(LcOutputType outputType, int outputNumber) {
        BidibCommand bidibCommand = new LcConfigXGetMessage(outputType, outputNumber);
        return initializeCommand(bidibCommand);
    }

    public BidibCommand createCommandStationAccessory(
        int address, AddressTypeEnum addressType, TimingControlEnum timingControl, ActivateCoilEnum activateCoil,
        int aspect, TimeBaseUnitEnum timeBaseUnit, int time) {
        BidibCommand bidibCommand =
            new CommandStationAccessoryMessage(address, addressType, timingControl, activateCoil, aspect, timeBaseUnit,
                time);
        return initializeCommand(bidibCommand);
    }
}
