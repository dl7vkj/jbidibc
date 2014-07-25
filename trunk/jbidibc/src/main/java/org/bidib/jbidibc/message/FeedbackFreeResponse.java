package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

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
