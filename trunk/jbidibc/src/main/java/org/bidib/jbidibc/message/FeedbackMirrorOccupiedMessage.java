package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class FeedbackMirrorOccupiedMessage extends BidibCommandMessage {
    public FeedbackMirrorOccupiedMessage(int detectorNumber) {
        super(0, BidibLibrary.MSG_BM_MIRROR_OCC, (byte) detectorNumber);
    }

    public String getName() {
        return "MSG_BM_MIRROR_OCC";
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
