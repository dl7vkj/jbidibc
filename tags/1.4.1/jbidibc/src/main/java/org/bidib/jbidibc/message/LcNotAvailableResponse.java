package org.bidib.jbidibc.message;

import org.bidib.jbidibc.exception.ProtocolException;

public class LcNotAvailableResponse extends BidibMessage {
    LcNotAvailableResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 2) {
            throw new ProtocolException("no lc not available received");
        }
    }
}
