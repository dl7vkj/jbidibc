package org.bidib.jbidibc.message;

import org.bidib.jbidibc.exception.ProtocolException;

public class FeedbackConfidenceResponse extends BidibMessage {
    FeedbackConfidenceResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 3) {
            throw new ProtocolException("no feedback confidence received");
        }
    }

    public int getValid() {
        return getData()[0];
    }

    public int getFreeze() {
        return getData()[1];
    }

    public int getSignal() {
        return getData()[2];
    }
}
