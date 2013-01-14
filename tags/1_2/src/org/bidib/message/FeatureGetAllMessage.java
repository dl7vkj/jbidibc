package org.bidib.message;

import org.bidib.BidibLibrary;

public class FeatureGetAllMessage extends BidibMessage {
    public FeatureGetAllMessage() {
        super(0, BidibLibrary.MSG_FEATURE_GETALL, null);
    }
}
