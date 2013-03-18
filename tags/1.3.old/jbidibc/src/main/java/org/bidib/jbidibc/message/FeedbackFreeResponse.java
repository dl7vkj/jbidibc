package org.bidib.jbidibc.message;

import org.bidib.jbidibc.exception.ProtocolException;

public class FeedbackFreeResponse extends BidibMessage {
    FeedbackFreeResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 1) {
            throw new ProtocolException("no feedback received");
        }
    }

    public int getDetectorNumber() {
        return getData()[0];
    }
}
