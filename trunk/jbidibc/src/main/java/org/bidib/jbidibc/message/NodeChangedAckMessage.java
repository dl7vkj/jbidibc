package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;

public class NodeChangedAckMessage extends BidibCommandMessage {
    public NodeChangedAckMessage(int versionNumber) {
        super(0, BidibLibrary.MSG_NODE_CHANGED_ACK, new byte[] { (byte) versionNumber });
    }

    public NodeChangedAckMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
