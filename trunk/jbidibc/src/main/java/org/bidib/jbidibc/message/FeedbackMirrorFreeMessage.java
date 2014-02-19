package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class FeedbackMirrorFreeMessage extends BidibMessage implements BidibCommand {
    public FeedbackMirrorFreeMessage(int detectorNumber) {
        super(0, BidibLibrary.MSG_BM_MIRROR_FREE, (byte) detectorNumber);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
