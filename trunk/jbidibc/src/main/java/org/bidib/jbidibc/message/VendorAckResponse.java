package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

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

    public int getReturnCode() {
        return ByteUtils.getInt(getData()[0]);
    }
}
