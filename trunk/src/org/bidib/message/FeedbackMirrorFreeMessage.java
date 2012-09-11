package org.bidib.message;

import org.bidib.BidibLibrary;

public class FeedbackMirrorFreeMessage extends BidibMessage {
    public FeedbackMirrorFreeMessage(int detectorNumber) {
        super(0, BidibLibrary.MSG_BM_MIRROR_FREE, (byte) detectorNumber);
    }
}
