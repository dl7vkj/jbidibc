package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;

/**
 * Command to get all feature settings
 */
public class FeatureGetAllMessage extends BidibCommandMessage {
    protected FeatureGetAllMessage() {
        super(0, BidibLibrary.MSG_FEATURE_GETALL);
    }

    public FeatureGetAllMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_FEATURE_GETALL";
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { FeatureCountResponse.TYPE };
    }
}
