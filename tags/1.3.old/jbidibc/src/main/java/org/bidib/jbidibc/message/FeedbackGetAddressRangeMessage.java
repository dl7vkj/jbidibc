package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class FeedbackGetAddressRangeMessage extends BidibMessage {
    /**
     * Get the loco addresses in the specified range from the feedback system.
     * @param begin the start of Melderbits to be transfered 
     * @param end the end of Melderbits to be transfered
     */
    public FeedbackGetAddressRangeMessage(int begin, int end) {
        super(0, BidibLibrary.MSG_BM_ADDR_GET_RANGE, new byte[] { (byte) begin, (byte) end });
    }
}
