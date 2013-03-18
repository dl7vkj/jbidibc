package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.Feature;
import org.bidib.jbidibc.exception.ProtocolException;

public class FeatureResponse extends BidibMessage {

    public static final Integer TYPE = BidibLibrary.MSG_FEATURE;

    FeatureResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 2) {
            throw new ProtocolException("no feature received");
        }
    }

    public Feature getFeature() {
        byte[] data = getData();

        return new Feature(data[0], data[1] & 0xFF);
    }
}
