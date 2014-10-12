package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.utils.ByteUtils;

public class FeedbackMirrorMultipleMessage extends BidibCommandMessage {
    public FeedbackMirrorMultipleMessage(int baseAddress, int size, byte[] detectorData) {
        super(0, BidibLibrary.MSG_BM_MIRROR_MULTIPLE, ByteUtils.concat(new byte[] { (byte) baseAddress, (byte) size },
            detectorData));
    }

    public String getName() {
        return "MSG_BM_MIRROR_MULTIPLE";
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
