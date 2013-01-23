package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class FeedbackMirrorMultipleMessage extends BidibMessage {
    public FeedbackMirrorMultipleMessage(int baseAddress, int size, byte[] detectorData) {
        super(0, BidibLibrary.MSG_BM_MIRROR_MULTIPLE, concat(new byte[] { (byte) baseAddress, (byte) size },
                detectorData));
    }
}
