package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

/**
 * Message to query the status of the booster
 */
public class BoostQueryMessage extends BidibMessage implements BidibCommand {
    public BoostQueryMessage() {
        super(0, BidibLibrary.MSG_BOOST_QUERY);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
