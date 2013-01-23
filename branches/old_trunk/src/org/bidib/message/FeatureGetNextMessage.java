package org.bidib.message;

import org.bidib.BidibLibrary;

public class FeatureGetNextMessage extends BidibMessage {
    public FeatureGetNextMessage() {
        super(0, BidibLibrary.MSG_FEATURE_GETNEXT, null);
    }
}
