package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class FeedbackGetAddressRangeMessage extends BidibMessage {
    public FeedbackGetAddressRangeMessage(int begin, int end) {
        super(0, BidibLibrary.MSG_BM_ADDR_GET_RANGE, new byte[] { (byte) begin, (byte) end });
    }
}
