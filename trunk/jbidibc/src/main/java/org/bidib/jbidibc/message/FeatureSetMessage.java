package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;

public class FeatureSetMessage extends BidibCommandMessage {
    protected FeatureSetMessage(int number, int value) {
        super(0, BidibLibrary.MSG_FEATURE_SET, new byte[] { (byte) number, (byte) value });
    }

    public FeatureSetMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { FeatureResponse.TYPE, FeatureNotAvailableResponse.TYPE };
    }
}
