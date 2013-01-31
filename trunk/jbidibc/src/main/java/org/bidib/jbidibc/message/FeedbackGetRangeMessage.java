package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class FeedbackGetRangeMessage extends BidibMessage {
    /**
     * Get the status of the feedback bits in the specified range.
     * @param begin the start of Melderbits to be transfered 
     * @param end the end of Melderbits to be transfered
     */
    public FeedbackGetRangeMessage(int begin, int end) {
        super(0, BidibLibrary.MSG_BM_GET_RANGE, new byte[] { (byte) begin, (byte) end });
    }
}
