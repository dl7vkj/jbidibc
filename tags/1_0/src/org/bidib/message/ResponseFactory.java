package org.bidib.message;

import org.bidib.BidibLibrary;
import org.bidib.exception.ProtocolException;

public class ResponseFactory {
    public static BidibMessage create(byte[] message) throws ProtocolException {
        BidibMessage result = new BidibMessage(message);

        if (result.getType() == (byte) BidibLibrary.MSG_FEATURE) {
            result = new FeatureResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
        } else if (result.getType() == (byte) BidibLibrary.MSG_FEATURE_COUNT) {
            result = new FeatureCountResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
        } else if (result.getType() == (byte) BidibLibrary.MSG_FEATURE_NA) {
            result = new FeatureNotAvailableResponse(result.getAddr(), result.getNum(), result.getType(),
                    result.getData());
        } else if (result.getType() == (byte) BidibLibrary.MSG_FW_UPDATE_STAT) {
            result = new FwUpdateStatResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
        } else if (result.getType() == (byte) BidibLibrary.MSG_LC_CONFIG) {
            result = new LcConfigResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
        } else if (result.getType() == (byte) BidibLibrary.MSG_LC_KEY) {
            result = new LcKeyResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
        } else if (result.getType() == (byte) BidibLibrary.MSG_LC_MACRO) {
            result = new LcMacroResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
        } else if (result.getType() == (byte) BidibLibrary.MSG_LC_MACRO_PARA) {
            result = new LcMacroParaResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
        } else if (result.getType() == (byte) BidibLibrary.MSG_LC_MACRO_STATE) {
            result = new LcMacroStateResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
        } else if (result.getType() == (byte) BidibLibrary.MSG_LC_NA) {
            result = new LcNotAvailableResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
        } else if (result.getType() == (byte) BidibLibrary.MSG_LC_STAT) {
            result = new LcStatResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
        } else if (result.getType() == (byte) BidibLibrary.MSG_LOGON) {
            result = new LogonResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
        } else if (result.getType() == (byte) BidibLibrary.MSG_NODETAB) {
            result = new NodeTabResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
        } else if (result.getType() == (byte) BidibLibrary.MSG_NODE_LOST) {
            result = new NodeLostResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
        } else if (result.getType() == (byte) BidibLibrary.MSG_NODE_NA) {
            result = new NodeNotAvailableResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
        } else if (result.getType() == (byte) BidibLibrary.MSG_NODE_NEW) {
            result = new NodeNewResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
        } else if (result.getType() == (byte) BidibLibrary.MSG_NODETAB_COUNT) {
            result = new NodeTabCountResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
        } else if (result.getType() == (byte) BidibLibrary.MSG_STALL) {
            result = new StallResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
        } else if (result.getType() == (byte) BidibLibrary.MSG_SYS_ERROR) {
            result = new SysErrorResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
        } else if (result.getType() == (byte) BidibLibrary.MSG_SYS_IDENTIFY_STATE) {
            result = new SysIdentifyResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
        } else if (result.getType() == (byte) BidibLibrary.MSG_SYS_MAGIC) {
            result = new SysMagicResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
        } else if (result.getType() == (byte) BidibLibrary.MSG_SYS_PONG) {
            result = new SysPongResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
        } else if (result.getType() == (byte) BidibLibrary.MSG_SYS_P_VERSION) {
            result = new SysPVersionResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
        } else if (result.getType() == (byte) BidibLibrary.MSG_SYS_SW_VERSION) {
            result = new SysSwVersionResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
        } else if (result.getType() == (byte) BidibLibrary.MSG_SYS_UNIQUE_ID) {
            result = new SysUniqueIdResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
        } else if (result.getType() == (byte) BidibLibrary.MSG_VENDOR) {
            result = new VendorResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
        } else if (result.getType() == (byte) BidibLibrary.MSG_VENDOR_ACK) {
            result = new VendorAckResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());
        } else {
            String msg = "got unknown response with type " + result.getType();

            System.err.println(msg);
            throw new ProtocolException(msg);
        }
        return result;
    }
}
