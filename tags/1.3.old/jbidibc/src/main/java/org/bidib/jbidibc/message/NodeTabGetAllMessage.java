package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class NodeTabGetAllMessage extends BidibMessage {
    public NodeTabGetAllMessage() {
        super(0, BidibLibrary.MSG_NODETAB_GETALL);
    }
}
