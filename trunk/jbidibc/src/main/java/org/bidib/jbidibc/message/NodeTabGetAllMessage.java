package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

/**
 * Message to start fetching the mapping table of unique id and local address from the interface.
 *
 */
public class NodeTabGetAllMessage extends BidibMessage {
    public NodeTabGetAllMessage() {
        super(0, BidibLibrary.MSG_NODETAB_GETALL);
    }
}
