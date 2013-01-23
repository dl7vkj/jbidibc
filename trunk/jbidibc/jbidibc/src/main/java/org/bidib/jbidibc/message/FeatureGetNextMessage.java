package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class FeatureGetNextMessage extends BidibMessage {
    public FeatureGetNextMessage() {
        super(0, BidibLibrary.MSG_FEATURE_GETNEXT);
    }
}
