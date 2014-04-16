package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.FeatureEnum;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeatureNotAvailableResponse extends BidibMessage {
    private static final Logger LOGGER = LoggerFactory.getLogger(FeatureNotAvailableResponse.class);

    public static final Integer TYPE = BidibLibrary.MSG_FEATURE_NA;

    FeatureNotAvailableResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 1) {
            throw new ProtocolException("no feature not available received");
        }
    }

    public FeatureNotAvailableResponse(byte[] addr, int num, int featureNum) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_FEATURE_NA, ByteUtils.getLowByte(featureNum));
    }

    /**
     * @return the feature number
     */
    public int getFeatureNumber() {
        return ByteUtils.getInt(getData()[0]);
    }

    /**
     * @return the feature number
     */
    public FeatureEnum getFeature() {
        FeatureEnum feature = null;
        byte number = getData()[0];
        try {
            feature = FeatureEnum.valueOf(number);
            LOGGER.debug("Prepared feature: {}", feature);
        }
        catch (IllegalArgumentException ex) {
            LOGGER.debug("Create feature request with unknown number: {}", number, ex);
        }
        return feature;
    }
}
