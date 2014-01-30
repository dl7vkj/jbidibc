package org.bidib.jbidibc.message;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestFactory.class);

    private static final Logger MSG_RAW_LOGGER = LoggerFactory.getLogger("RAW");

    private RequestFactory() {
    }

    public static List<BidibMessage> create(byte[] message) throws ProtocolException {
        LOGGER.debug("Create bidib message from raw message: {}", message);

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        List<BidibMessage> bidibMessages = new ArrayList<BidibMessage>();

        boolean escapeHot = false;
        StringBuilder logRecord = new StringBuilder();

        // read the values from in the port
        for (byte data : message) {
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
                byte[] result = output.toByteArray();
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("prepared user data: {}, total len: {}", ByteUtils.bytesToHex(result), result.length);
                }
                // read the first byte that is the length of the message
                byte len = result[0];
                if ((len + 1) < result.length - 1) {
                    // we have multiple messages
                    int index = 0;
                    while (index < result.length - 1) {
                        len = result[index];
                        byte[] part = new byte[len + 1];
                        System.arraycopy(result, index, part, 0, len + 1);
                        BidibMessage bidibMessage = createConcreteMessage(new BidibMessage(part), part);
                        bidibMessages.add(bidibMessage);
                        LOGGER.trace("Added new bidibMessage: {}, index: {}", bidibMessage, index);
                        index += len + 1;
                    }
                }
                else {
                    // we have a single messages
                    BidibMessage bidibMessage = createConcreteMessage(new BidibMessage(result), result);
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

    private static BidibMessage createConcreteMessage(BidibMessage bidibMessage, byte[] message)
        throws ProtocolException {
        BidibMessage concreteBidibMessage = null;
        switch (ByteUtils.getInt(bidibMessage.getType())) {
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
            case BidibLibrary.MSG_CS_SET_STATE:
                concreteBidibMessage = new CommandStationSetStateMessage(message);
                break;
            case BidibLibrary.MSG_CS_POM:
                concreteBidibMessage = new CommandStationPomMessage(message);
                break;
            case BidibLibrary.MSG_FEATURE_GET:
                concreteBidibMessage = new FeatureGetMessage(message);
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
            case BidibLibrary.MSG_LC_KEY_QUERY:
                concreteBidibMessage = new LcKeyMessage(message);
                break;
            case BidibLibrary.MSG_LC_OUTPUT_QUERY:
                concreteBidibMessage = new LcOutputQueryMessage(message);
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
            case BidibLibrary.MSG_FW_UPDATE_OP:
                concreteBidibMessage = new FwUpdateOpMessage(message);
                break;

            default:
                concreteBidibMessage = bidibMessage;
                break;
        }
        return concreteBidibMessage;
    }
}
