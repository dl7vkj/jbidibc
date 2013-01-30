package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

/**
 * Command to switch track voltage (signal) on
 */
public class BoostOnMessage extends BidibMessage {
    public BoostOnMessage() {
        super(0, BidibLibrary.MSG_BOOST_ON);
    }
}
