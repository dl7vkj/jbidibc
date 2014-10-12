package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;

public class NodeNotAvailableResponse extends BidibMessage {

    public static final Integer TYPE = BidibLibrary.MSG_NODE_NA;

    NodeNotAvailableResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 1) {
            throw new ProtocolException("no node not available received");
        }
    }

    public String getName() {
        return "MSG_NODE_NA";
    }
}
