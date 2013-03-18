package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class NodeChangedAckMessage extends BidibMessage {
    public NodeChangedAckMessage(int versionNumber) {
        super(0, BidibLibrary.MSG_NODE_CHANGED_ACK, new byte[] { (byte) versionNumber });
    }
}
