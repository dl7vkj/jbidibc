package org.bidib.message;

import org.bidib.BidibLibrary;

public class FeatureSetMessage extends BidibMessage {
    public FeatureSetMessage(int number, int value) {
        super(0, BidibLibrary.MSG_FEATURE_SET, new byte[] { (byte) number, (byte) value });
    }
}
