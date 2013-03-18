package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

/**
 * Command to switch track voltage (signal) off
 */
public class BoostOffMessage extends BidibMessage {
    public BoostOffMessage() {
        super(0, BidibLibrary.MSG_BOOST_OFF);
    }
}
