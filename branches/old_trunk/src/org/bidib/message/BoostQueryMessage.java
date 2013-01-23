package org.bidib.message;

import org.bidib.BidibLibrary;

public class BoostQueryMessage extends BidibMessage {
    public BoostQueryMessage() {
        super(0, BidibLibrary.MSG_BOOST_QUERY, null);
    }
}
