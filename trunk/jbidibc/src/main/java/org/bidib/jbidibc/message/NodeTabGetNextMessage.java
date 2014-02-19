package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class NodeTabGetNextMessage extends BidibMessage implements BidibCommand {
    public NodeTabGetNextMessage() {
        super(0, BidibLibrary.MSG_NODETAB_GETNEXT);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { NodeTabResponse.TYPE };
    }
}
