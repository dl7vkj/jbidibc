package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class BoostQueryMessage extends BidibMessage {
    public BoostQueryMessage() {
        super(0, BidibLibrary.MSG_BOOST_QUERY, null);
    }
}
