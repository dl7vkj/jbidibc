package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

/**
 * Response to the feature getAll request
 */
public class FeatureCountResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_FEATURE_COUNT;

    FeatureCountResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 1) {
            throw new ProtocolException("no feature count received");
        }
    }

    public FeatureCountResponse(byte[] addr, int num, int featureCount) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_FEATURE_COUNT, ByteUtils.getLowByte(featureCount));
    }

    public int getCount() {
        return ByteUtils.getInt(getData()[0]);
    }
}
