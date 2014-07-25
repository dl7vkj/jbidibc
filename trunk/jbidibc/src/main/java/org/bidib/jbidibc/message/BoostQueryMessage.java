package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;

/**
 * Message to query the status of the booster
 */
public class BoostQueryMessage extends BidibCommandMessage {
    public static final Integer TYPE = BidibLibrary.MSG_BOOST_QUERY;

    public BoostQueryMessage() {
        super(0, BidibLibrary.MSG_BOOST_QUERY);
    }

    public BoostQueryMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_BOOST_QUERY";
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
