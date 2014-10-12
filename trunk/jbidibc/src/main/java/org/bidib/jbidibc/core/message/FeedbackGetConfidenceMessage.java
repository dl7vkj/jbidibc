package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;

public class FeedbackGetConfidenceMessage extends BidibCommandMessage {
    public FeedbackGetConfidenceMessage() {
        super(0, BidibLibrary.MSG_BM_GET_CONFIDENCE);
    }

    public FeedbackGetConfidenceMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_BM_GET_CONFIDENCE";
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
