package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

/**
 * Command to get all feature settings
 */
public class FeatureGetAllMessage extends BidibCommandMessage {
    public FeatureGetAllMessage() {
        super(0, BidibLibrary.MSG_FEATURE_GETALL);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { FeatureCountResponse.TYPE };
    }
}
