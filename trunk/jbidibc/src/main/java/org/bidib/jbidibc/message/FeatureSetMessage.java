package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class FeatureSetMessage extends BidibCommandMessage {
    public FeatureSetMessage(int number, int value) {
        super(0, BidibLibrary.MSG_FEATURE_SET, new byte[] { (byte) number, (byte) value });
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { FeatureResponse.TYPE, FeatureNotAvailableResponse.TYPE };
    }
}
