package org.bidib.message;

import org.bidib.BidibLibrary;

public class BoostOffMessage extends BidibMessage {
    public BoostOffMessage() {
        super(0, BidibLibrary.MSG_BOOST_ON, null);
    }
}
