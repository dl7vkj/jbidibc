package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;

public class StallResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_STALL;

    StallResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data != null && data.length != 1) {
            throw new ProtocolException("invalid data in stall response: " + ByteUtils.bytesToHex(data));
        }
    }

    public StallResponse(byte[] addr, int num, byte data) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_STALL, data);
    }

    public String getName() {
        return "MSG_STALL";
    }

    public boolean isStall() {
        byte[] data = getData();
        return (data[0] == 1 ? true : false);
    }
}
