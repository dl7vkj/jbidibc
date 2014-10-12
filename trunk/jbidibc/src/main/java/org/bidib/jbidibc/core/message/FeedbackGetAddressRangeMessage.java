package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;

public class FeedbackGetAddressRangeMessage extends BidibCommandMessage {
    /**
     * Get the loco addresses in the specified range from the feedback system.
     * 
     * @param begin
     *            the start of Melderbits to be transfered
     * @param end
     *            the end of Melderbits to be transfered
     */
    public FeedbackGetAddressRangeMessage(int begin, int end) {
        super(0, BidibLibrary.MSG_BM_ADDR_GET_RANGE, new byte[] { (byte) begin, (byte) end });
    }

    public FeedbackGetAddressRangeMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_BM_ADDR_GET_RANGE";
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
