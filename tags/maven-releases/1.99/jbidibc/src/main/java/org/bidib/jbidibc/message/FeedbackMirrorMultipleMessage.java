package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class FeedbackMirrorMultipleMessage extends BidibMessage {
    public FeedbackMirrorMultipleMessage(int baseAddress, int size, byte[] detectorData) {
        super(0, BidibLibrary.MSG_BM_MIRROR_MULTIPLE, join((byte) baseAddress, (byte) size, detectorData));
    }

    private static byte[] join(byte b1, byte b2, byte[] bytes) {
        byte[] result = new byte[bytes != null ? bytes.length + 2 : 2];

        result[0] = b1;
        result[1] = b2;
        if (bytes != null) {
            System.arraycopy(bytes, 0, result, 2, bytes.length);
        }
        return result;
    }
}
