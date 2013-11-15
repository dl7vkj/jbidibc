package org.bidib.jbidibc.message;

import org.bidib.jbidibc.exception.ProtocolException;

public class NodeNotAvailableResponse extends BidibMessage {
    NodeNotAvailableResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 1) {
            throw new ProtocolException("no node not available received");
        }
    }
}
