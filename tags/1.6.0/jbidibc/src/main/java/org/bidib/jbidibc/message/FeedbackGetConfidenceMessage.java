package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;

public class FeedbackGetConfidenceMessage extends BidibCommandMessage {
    public FeedbackGetConfidenceMessage() {
        super(0, BidibLibrary.MSG_BM_GET_CONFIDENCE);
    }

    public FeedbackGetConfidenceMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
