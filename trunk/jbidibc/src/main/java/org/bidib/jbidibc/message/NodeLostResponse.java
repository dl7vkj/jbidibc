package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;

public class NodeLostResponse extends NodeTabResponse {
    public static final Integer TYPE = BidibLibrary.MSG_NODE_LOST;

    NodeLostResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
    }

    public String getName() {
        return "MSG_NODE_LOST";
    }
}
