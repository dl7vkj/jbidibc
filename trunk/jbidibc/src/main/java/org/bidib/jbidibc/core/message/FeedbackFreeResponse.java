package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;

public class FeedbackFreeResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_BM_FREE;

    FeedbackFreeResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 1) {
            throw new ProtocolException("no feedback received");
        }
    }

    public String getName() {
        return "MSG_BM_FREE";
    }

    public int getDetectorNumber() {
        return ByteUtils.getInt(getData()[0]);
    }
}
