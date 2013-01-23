package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class NodeTabGetNextMessage extends BidibMessage {
    public NodeTabGetNextMessage() {
        super(0, BidibLibrary.MSG_NODETAB_GETNEXT);
    }
}
