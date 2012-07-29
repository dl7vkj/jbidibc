package org.bidib.message;

import org.bidib.BidibLibrary;

public class NodeTabGetAllMessage extends BidibMessage {
    public NodeTabGetAllMessage() {
        super(0, BidibLibrary.MSG_NODETAB_GETALL, null);
    }
}
