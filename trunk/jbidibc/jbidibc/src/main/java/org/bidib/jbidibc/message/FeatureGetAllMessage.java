package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class FeatureGetAllMessage extends BidibMessage {
    public FeatureGetAllMessage() {
        super(0, BidibLibrary.MSG_FEATURE_GETALL);
    }
}
