package org.bidib.message;

import org.bidib.BidibLibrary;

public class FeedbackMirrorOccupiedMessage extends BidibMessage {
    public FeedbackMirrorOccupiedMessage(int detectorNumber) {
        super(0, BidibLibrary.MSG_BM_MIRROR_OCC, (byte) detectorNumber);
    }
}
