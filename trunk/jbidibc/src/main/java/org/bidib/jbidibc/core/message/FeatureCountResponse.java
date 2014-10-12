package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;

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

    public String getName() {
        return "MSG_FEATURE_COUNT";
    }

    public int getCount() {
        return ByteUtils.getInt(getData()[0]);
    }
}
