package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;

public class FeatureGetNextMessage extends BidibCommandMessage {
    protected FeatureGetNextMessage() {
        super(0, BidibLibrary.MSG_FEATURE_GETNEXT);
    }

    public FeatureGetNextMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_FEATURE_GETNEXT";
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { FeatureResponse.TYPE, FeatureNotAvailableResponse.TYPE };
    }
}
