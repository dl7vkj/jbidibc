package org.bidib.message;

import org.bidib.exception.ProtocolException;

public class VendorAckResponse extends BidibMessage {
    VendorAckResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length < 1) {
            throw new ProtocolException("no vendor ack received");
        }
    }

    public int getReturnCode() {
        return getData()[0];
    }
}
