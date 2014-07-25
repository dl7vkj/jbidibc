package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
import org.bidib.jbidibc.utils.MessageUtils;

public class FeedbackCurrentResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_BM_CURRENT;

    FeedbackCurrentResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 1) {
            throw new ProtocolException("no feedback received");
        }
    }

    public String getName() {
        return "MSG_BM_CURRENT";
    }

    public int getCurrent() {
        return MessageUtils.convertCurrent(ByteUtils.getInt(getData()[0]));
    }

    public int getLocalDetectorAddress() {
        byte[] data = getData();

        return ByteUtils.getInt(data[0]);
    }
}
