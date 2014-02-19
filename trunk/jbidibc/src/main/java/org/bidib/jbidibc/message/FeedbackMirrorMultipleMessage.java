package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.utils.ByteUtils;

public class FeedbackMirrorMultipleMessage extends BidibCommandMessage {
    public FeedbackMirrorMultipleMessage(int baseAddress, int size, byte[] detectorData) {
        super(0, BidibLibrary.MSG_BM_MIRROR_MULTIPLE, ByteUtils.concat(new byte[] { (byte) baseAddress, (byte) size },
            detectorData));
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
