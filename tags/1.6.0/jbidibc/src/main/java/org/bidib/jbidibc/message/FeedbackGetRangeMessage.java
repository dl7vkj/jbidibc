package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;

public class FeedbackGetRangeMessage extends BidibCommandMessage {
    /**
     * Get the status of the feedback bits in the specified range.
     * 
     * @param begin
     *            the start of Melderbits to be transfered
     * @param end
     *            the end of Melderbits to be transfered
     */
    public FeedbackGetRangeMessage(int begin, int end) {
        super(0, BidibLibrary.MSG_BM_GET_RANGE, new byte[] { (byte) begin, (byte) end });
    }

    public FeedbackGetRangeMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public byte getBegin() {
        return getData()[0];
    }

    public byte getEnd() {
        return getData()[1];
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
