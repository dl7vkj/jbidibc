package org.bidib.message;

import org.bidib.exception.ProtocolException;

public class SysErrorResponse extends BidibMessage {
    SysErrorResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length < 1) {
            throw new ProtocolException("no error received");
        }
    }

    public int getErrorCode() {
        return getData()[0];
    }
}
