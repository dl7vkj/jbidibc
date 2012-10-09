package org.bidib.message;

import org.bidib.exception.ProtocolException;

public class LcWaitResponse extends BidibMessage {
    LcWaitResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 3) {
            throw new ProtocolException("no lc wait received");
        }
    }
}
