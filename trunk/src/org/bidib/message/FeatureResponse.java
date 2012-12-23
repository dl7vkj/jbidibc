package org.bidib.message;

import org.bidib.Feature;
import org.bidib.exception.ProtocolException;

public class FeatureResponse extends BidibMessage {
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
