package org.bidib.message;

import org.bidib.BidibLibrary;

public class FeedbackGetRangeMessage extends BidibMessage {
    public FeedbackGetRangeMessage(int begin, int end) {
        super(0, BidibLibrary.MSG_BM_GET_RANGE, new byte[] { (byte) begin, (byte) end });
    }
}
