package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ResponseFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseFactory.class);

    private ResponseFactory() {
    }

    public static BidibMessage create(byte[] message) throws ProtocolException {
        LOGGER.debug("Create bidib message from raw message: {}", message);

        BidibMessage result = new BidibMessage(message);
        int type = ByteUtils.getInt(result.getType());
        switch (type) {
            case BidibLibrary.MSG_ACCESSORY_PARA:
                result =
                    new AccessoryParaResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_ACCESSORY_STATE:
                result =
                    new AccessoryStateResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_BM_ADDRESS:
                result =
                    new FeedbackAddressResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_BM_CONFIDENCE:
                result =
                    new FeedbackConfidenceResponse(result.getAddr(), result.getNum(), result.getType(), result
                        .getData());
                break;
            case BidibLibrary.MSG_BM_CURRENT:
                result =
                    new FeedbackCurrentResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_BM_FREE:
                result =
                    new FeedbackFreeResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_BM_MULTIPLE:
                result =
                    new FeedbackMultipleResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_BM_OCC:
                result =
                    new FeedbackOccupiedResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_BM_SPEED:
                result =
                    new FeedbackSpeedResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_BOOST_CURRENT:
                result =
                    new BoostCurrentResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_BOOST_DIAGNOSTIC:
                result =
                    new BoostDiagnosticResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_BOOST_STAT:
                result = new BoostStatResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_CS_DRIVE_ACK:
                result =
                    new CommandStationDriveAcknowledgeResponse(result.getAddr(), result.getNum(), result.getType(),
                        result.getData());
                break;
            case BidibLibrary.MSG_CS_DRIVE_MANUAL:
                // drive manual
                result =
                    new CommandStationDriveManualResponse(result.getAddr(), result.getNum(), result.getType(), result
                        .getData());
                break;
            case BidibLibrary.MSG_CS_DRIVE_EVENT:
                // drive event
                result =
                    new CommandStationDriveEventResponse(result.getAddr(), result.getNum(), result.getType(), result
                        .getData());
                break;
            case BidibLibrary.MSG_CS_STATE:
                result =
                    new CommandStationStateResponse(result.getAddr(), result.getNum(), result.getType(), result
                        .getData());
                break;
            case BidibLibrary.MSG_CS_ACCESSORY_ACK:
                result =
                    new CommandStationAccessoryAcknowledgeResponse(result.getAddr(), result.getNum(), result.getType(),
                        result.getData());
                break;
            case BidibLibrary.MSG_CS_POM_ACK:
                result =
                    new CommandStationPomAcknowledgeResponse(result.getAddr(), result.getNum(), result.getType(),
                        result.getData());
                break;
            case BidibLibrary.MSG_FEATURE:
                result = new FeatureResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_FEATURE_COUNT:
                result =
                    new FeatureCountResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_FEATURE_NA:
                result =
                    new FeatureNotAvailableResponse(result.getAddr(), result.getNum(), result.getType(), result
                        .getData());
                break;
            case BidibLibrary.MSG_FW_UPDATE_STAT:
                result =
                    new FwUpdateStatResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_LC_CONFIG:
                result = new LcConfigResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_LC_KEY:
                result = new LcKeyResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_LC_MACRO:
                result = new LcMacroResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_LC_MACRO_PARA:
                result = new LcMacroParaResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_LC_MACRO_STATE:
                result =
                    new LcMacroStateResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_LC_NA:
                result =
                    new LcNotAvailableResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_LC_STAT:
                result = new LcStatResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_LC_WAIT:
                result = new LcWaitResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_LOGON:
                result = new LogonResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_NODETAB:
                result = new NodeTabResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_NODE_LOST:
                result = new NodeLostResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_NODE_NA:
                result =
                    new NodeNotAvailableResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_NODE_NEW:
                result = new NodeNewResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_NODETAB_COUNT:
                result =
                    new NodeTabCountResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_STALL:
                result = new StallResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_SYS_ERROR:
                result = new SysErrorResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_SYS_IDENTIFY_STATE:
                result = new SysIdentifyResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_SYS_MAGIC:
                result = new SysMagicResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_SYS_PONG:
                result = new SysPongResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_SYS_P_VERSION:
                result = new SysPVersionResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_SYS_SW_VERSION:
                result =
                    new SysSwVersionResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_SYS_UNIQUE_ID:
                result = new SysUniqueIdResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_VENDOR:
                result = new VendorResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_VENDOR_ACK:
                result = new VendorAckResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_NEW_DECODER:
                result = new NewDecoderResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_ID_SEARCH_ACK:
                result = new IdSearchAckResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            case BidibLibrary.MSG_ADDR_CHANGE_ACK:
                result =
                    new AddrChangeAckResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
                break;
            default:
                LOGGER.error("Got unknown response with type '{}', message: {}", String.format("0x%02x", result
                    .getType()), message);

                String msg = "got unknown response with type " + (result.getType() & 0xFF);
                // TODO remove this ... and ignore unknown message
                throw new ProtocolException(msg);
        }
        return result;
    }
}
