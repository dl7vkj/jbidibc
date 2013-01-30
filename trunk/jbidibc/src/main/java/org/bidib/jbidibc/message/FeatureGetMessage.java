package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

/**
 * Command to get feature settings of a specific feature
 */
public class FeatureGetMessage extends BidibMessage {
    public FeatureGetMessage(int number) {
        super(0, BidibLibrary.MSG_FEATURE_GET, new byte[] { (byte) number });
    }
}
