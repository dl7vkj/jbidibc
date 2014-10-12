package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;

public class FeedbackOccupiedResponse extends BidibMessage {
    FeedbackOccupiedResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 1) {
            throw new ProtocolException("no feedback received");
        }
    }

    public FeedbackOccupiedResponse(byte[] addr, int num, int detectorAddress) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_BM_OCC, ByteUtils.getLowByte(detectorAddress));
    }

    public String getName() {
        return "MSG_BM_OCC";
    }

    public int getDetectorNumber() {
        return getData()[0];
    }
}
