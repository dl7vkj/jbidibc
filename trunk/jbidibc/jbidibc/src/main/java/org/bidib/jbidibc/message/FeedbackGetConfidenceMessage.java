package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class FeedbackGetConfidenceMessage extends BidibMessage {
    public FeedbackGetConfidenceMessage() {
        super(0, BidibLibrary.MSG_BM_GET_CONFIDENCE);
    }
}
