package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestFactory.class);

    private RequestFactory() {
    }

    public static BidibMessage create(byte[] message) throws ProtocolException {
        LOGGER.debug("Create bidib message from raw message: {}", message);

        int len = message.length - 1;
        int startIndex = 0;
        int endIndex = len;
        if (message[0] == (byte) 0xFE) {
            startIndex++;
        }
        if (message[len] == (byte) 0xFE) {
            endIndex--;
        }
        byte[] result = new byte[endIndex - startIndex];
        System.arraycopy(message, startIndex, result, 0, result.length);

        BidibMessage bidibMessage = createConcreteMessage(new BidibMessage(result), result);
        return bidibMessage;
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
            case BidibLibrary.MSG_FEATURE_GET:
                concreteBidibMessage = new FeatureGetMessage(message);
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
                concreteBidibMessage = bidibMessage;
                break;
        }
        return concreteBidibMessage;
    }
}
