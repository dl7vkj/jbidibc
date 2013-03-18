package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

/**
 * Message to query the status of the booster
 */
public class BoostQueryMessage extends BidibMessage {
    public BoostQueryMessage() {
        super(0, BidibLibrary.MSG_BOOST_QUERY);
    }
}
