package org.bidib.message;

import org.bidib.exception.ProtocolException;

public class LcWaitResponse extends BidibMessage {
    LcWaitResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 3) {
            throw new ProtocolException("no lc wait received");
        }
    }

    public int getTimeout() {
        int result = getData()[2] & 0xFF;

        return result > 127 ? result * 1000 : result * 100;
    }
}
