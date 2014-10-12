package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;

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
