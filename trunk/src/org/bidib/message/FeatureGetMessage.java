package org.bidib.message;

import org.bidib.BidibLibrary;

public class FeatureGetMessage extends BidibMessage {
    public FeatureGetMessage(int number) {
        super(0, BidibLibrary.MSG_FEATURE_GET, new byte[] { (byte) number });
    }
}
