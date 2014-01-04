package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeedbackConfidenceResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_BM_CONFIDENCE;

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackConfidenceResponse.class);

    FeedbackConfidenceResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 3) {
            throw new ProtocolException("no feedback confidence received");
        }
        LOGGER.debug("Received confidence response, valid: {}, freeze: {}, signal: {}", getValid(), getFreeze(),
            getSignal());
    }

    public FeedbackConfidenceResponse(byte[] addr, int num, byte valid, byte freeze, byte signal)
        throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_BM_CONFIDENCE, new byte[] { valid, freeze, signal });
    }

    public int getValid() {
        return ByteUtils.getInt(getData()[0]);
    }

    public int getFreeze() {
        return ByteUtils.getInt(getData()[1]);
    }

    public int getSignal() {
        return ByteUtils.getInt(getData()[2]);
    }

}
