package org.bidib.message;

import org.bidib.exception.ProtocolException;

public class SysPongResponse extends BidibMessage {
    SysPongResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data != null && data.length > 0) {
            throw new ProtocolException("invalid data in pong response");
        }
    }
}
