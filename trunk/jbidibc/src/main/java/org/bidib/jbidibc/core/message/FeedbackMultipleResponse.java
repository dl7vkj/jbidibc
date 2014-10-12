package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;

public class FeedbackMultipleResponse extends BidibMessage {
    FeedbackMultipleResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length < 3) {
            throw new ProtocolException("no feedback received");
        }
    }

    public FeedbackMultipleResponse(byte[] addr, int num, byte baseAddress, byte feedbackSize, byte[] feedbackMultiple)
        throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_BM_MULTIPLE, ByteUtils.concat(new byte[] { baseAddress, feedbackSize },
            feedbackMultiple));
    }

    public String getName() {
        return "MSG_BM_MULTIPLE";
    }

    public int getBaseAddress() {
        return ByteUtils.getInt(getData()[0]);
    }

    public byte[] getDetectorData() {
        byte[] data = getData();
        byte[] result = new byte[data.length - 2];

        System.arraycopy(data, 2, result, 0, result.length);
        return result;
    }

    public int getSize() {
        return ByteUtils.getInt(getData()[1]);
    }
}
