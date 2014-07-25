package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;

public class NodeTabGetNextMessage extends BidibCommandMessage {
    protected NodeTabGetNextMessage() {
        super(0, BidibLibrary.MSG_NODETAB_GETNEXT);
    }

    public NodeTabGetNextMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_NODETAB_GETNEXT";
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { NodeTabResponse.TYPE };
    }
}
