package org.bidib.jbidibc.message;

import org.bidib.jbidibc.exception.ProtocolException;

/**
 * Response to the feature getAll request
 */
public class FeatureCountResponse extends BidibMessage {
    FeatureCountResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 1) {
            throw new ProtocolException("no feature count received");
        }
    }

    public int getCount() {
        return getData()[0];
    }
}
