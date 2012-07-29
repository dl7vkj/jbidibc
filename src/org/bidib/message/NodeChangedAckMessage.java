package org.bidib.message;

import org.bidib.BidibLibrary;

public class NodeChangedAckMessage extends BidibMessage {
    public NodeChangedAckMessage(int versionNumber) {
        super(0, BidibLibrary.MSG_NODE_CHANGED_ACK, new byte[] { (byte) versionNumber });
    }
}
