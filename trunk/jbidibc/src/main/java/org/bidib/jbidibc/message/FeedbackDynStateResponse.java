package org.bidib.jbidibc.message;

import java.io.ByteArrayOutputStream;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

/**
 * Signal the detection of a dynamic state from loco in the specified section. 
 */
public class FeedbackDynStateResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_BM_DYN_STATE;

    FeedbackDynStateResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 2) {
            throw new ProtocolException("no feedback dynamic state received");
        }
    }

    public FeedbackDynStateResponse(byte[] addr, int num, int detectorNumber, byte dynNumber, byte dynValue)
        throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_BM_DYN_STATE, prepareDynamicStateData(detectorNumber, dynNumber, dynValue));
    }

    public int getDetectorNumber() {
        return ByteUtils.getInt(getData()[0], 0x7F);
    }

    private static byte[] prepareDynamicStateData(int detectorNumber, byte dynNumber, byte dynValue) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(ByteUtils.getLowByte(detectorNumber));

        out.write(dynNumber);
        out.write(dynValue);

        return out.toByteArray();
    }

    public int getDynNumber() {
        return ByteUtils.getInt(getData()[1], 0xFF);
    }

    public int getDynValue() {
        return ByteUtils.getInt(getData()[2], 0xFF);
    }
}
