package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class FeatureGetNextMessage extends BidibCommandMessage {
    public FeatureGetNextMessage() {
        super(0, BidibLibrary.MSG_FEATURE_GETNEXT);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { FeatureResponse.TYPE, FeatureNotAvailableResponse.TYPE };
    }
}
