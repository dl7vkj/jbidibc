package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.Feature;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;

public class FeatureResponse extends BidibMessage {

    public static final Integer TYPE = BidibLibrary.MSG_FEATURE;

    FeatureResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 2) {
            throw new ProtocolException("no feature received");
        }
    }

    public FeatureResponse(byte[] addr, int num, int featureNum, int featureValue) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_FEATURE, ByteUtils.getLowByte(featureNum), ByteUtils.getLowByte(featureValue));
    }

    public String getName() {
        return "MSG_FEATURE";
    }

    public Feature getFeature() {
        byte[] data = getData();

        return new Feature(ByteUtils.getInt(data[0]), ByteUtils.getInt(data[1]));
    }
}
