package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

/**
 * Signal the block CV message from feedback device.
 */
public class FeedbackBlockCvResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_BM_BLOCK_CV;

    FeedbackBlockCvResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 12) {
            throw new ProtocolException("no feedback block CV received");
        }
    }

    // public FeedbackBlockCvResponse(byte[] addr, int num, int address, int cvNumber, byte dat)
    // throws ProtocolException {
    // this(addr, num, BidibLibrary.MSG_BM_BLOCK_CV, prepareData(address, cvNumber & 0x3FF, dat));
    // }

    public int getDeecoderDeviceId() {
        return ByteUtils.getInt(getData()[0]);
    }

    // private static byte[] prepareData(int address, int cvNumber, byte dat) {
    // ByteArrayOutputStream out = new ByteArrayOutputStream();
    // out.write(ByteUtils.getLowByte(address));
    // out.write(ByteUtils.getHighByte(address) & 0x3F);
    //
    // out.write(ByteUtils.getLowByte(cvNumber));
    // out.write(ByteUtils.getHighByte(cvNumber));
    //
    // out.write(dat);
    // return out.toByteArray();
    // }
    // public int getDat() {
    // return ByteUtils.getInt(getData()[4]);
    // }
}
