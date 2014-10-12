package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;

public class FeedbackMirrorFreeMessage extends BidibCommandMessage {
    public FeedbackMirrorFreeMessage(int detectorNumber) {
        super(0, BidibLibrary.MSG_BM_MIRROR_FREE, (byte) detectorNumber);
    }

    public String getName() {
        return "MSG_BM_MIRROR_FREE";
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
