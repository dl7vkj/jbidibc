package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;

/**
 * Message to start fetching the mapping table of unique id and local address from the interface.
 * 
 */
public class NodeTabGetAllMessage extends BidibCommandMessage {
    protected NodeTabGetAllMessage() {
        super(0, BidibLibrary.MSG_NODETAB_GETALL);
    }

    public NodeTabGetAllMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_NODETAB_GETALL";
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { NodeTabCountResponse.TYPE };
    }
}
