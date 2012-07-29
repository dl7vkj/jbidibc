package org.bidib.message;

import org.bidib.exception.ProtocolException;

public class NodeTabCountResponse extends BidibMessage {
    NodeTabCountResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 1) {
            throw new ProtocolException("no node tab count received");
        }
    }

    public int getCount() {
        return getData()[0];
    }
}
