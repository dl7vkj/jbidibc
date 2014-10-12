package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.bidib.jbidibc.core.utils.MessageUtils;

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
