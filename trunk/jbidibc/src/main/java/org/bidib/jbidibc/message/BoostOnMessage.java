package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class BoostOnMessage extends BidibMessage {
    public BoostOnMessage() {
        super(0, BidibLibrary.MSG_BOOST_ON);
    }
}
