package org.bidib.message;

import org.bidib.BidibLibrary;

public class NodeTabGetNextMessage extends BidibMessage {
    public NodeTabGetNextMessage() {
        super(0, BidibLibrary.MSG_NODETAB_GETNEXT, null);
    }
}
