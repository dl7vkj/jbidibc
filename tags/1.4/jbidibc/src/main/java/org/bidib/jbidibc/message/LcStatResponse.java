package org.bidib.jbidibc.message;

import org.bidib.jbidibc.exception.ProtocolException;

public class LcStatResponse extends BidibMessage {
    LcStatResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 3) {
            throw new ProtocolException("no lc stat received");
        }
    }
}
