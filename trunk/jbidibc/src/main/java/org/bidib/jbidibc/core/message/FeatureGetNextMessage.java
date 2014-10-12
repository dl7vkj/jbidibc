package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;

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
