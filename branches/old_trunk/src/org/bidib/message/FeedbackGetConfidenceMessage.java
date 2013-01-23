package org.bidib.message;

import org.bidib.BidibLibrary;

public class FeedbackGetConfidenceMessage extends BidibMessage {
    public FeedbackGetConfidenceMessage() {
        super(0, BidibLibrary.MSG_BM_GET_CONFIDENCE);
    }
}
