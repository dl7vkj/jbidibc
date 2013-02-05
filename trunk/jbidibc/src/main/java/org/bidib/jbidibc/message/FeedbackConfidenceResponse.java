package org.bidib.jbidibc.message;

import org.bidib.jbidibc.exception.ProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeedbackConfidenceResponse extends BidibMessage {
    private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackConfidenceResponse.class);

    FeedbackConfidenceResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 3) {
            throw new ProtocolException("no feedback confidence received");
        }
        LOGGER.debug("Received confidence response, valid: {}, freeze: {}, signal: {}", getValid(), getFreeze(),
            getSignal());
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
