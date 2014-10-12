package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;

public class NodeChangedAckMessage extends BidibCommandMessage {
    public NodeChangedAckMessage(int versionNumber) {
        super(0, BidibLibrary.MSG_NODE_CHANGED_ACK, new byte[] { (byte) versionNumber });
    }

    public NodeChangedAckMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_NODE_CHANGED_ACK";
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
