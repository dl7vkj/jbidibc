package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;

public class VendorAckResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_VENDOR_ACK;

    public static final int USER_CONFIG_MODE_OFF = 0;

    public static final int USER_CONFIG_MODE_ON = 1;

    VendorAckResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length < 1) {
            throw new ProtocolException("no vendor ack received");
        }
    }

    public VendorAckResponse(byte[] addr, int num, byte userConfigMode) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_VENDOR_ACK, userConfigMode);
    }

    public String getName() {
        return "MSG_VENDOR_ACK";
    }

    public int getReturnCode() {
        return ByteUtils.getInt(getData()[0]);
    }
}
