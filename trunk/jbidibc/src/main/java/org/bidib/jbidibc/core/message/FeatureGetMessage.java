package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.enumeration.FeatureEnum;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command to get feature settings of a specific feature
 */
public class FeatureGetMessage extends BidibCommandMessage {
    private static final Logger LOGGER = LoggerFactory.getLogger(FeatureGetMessage.class);

    protected FeatureGetMessage(int number) {
        super(0, BidibLibrary.MSG_FEATURE_GET, ByteUtils.getLowByte(number));

        try {
            LOGGER.debug("Prepared get feature: {}", FeatureEnum.valueOf(ByteUtils.getLowByte(number)));
        }
        catch (IllegalArgumentException ex) {
            LOGGER.debug("Create feature request with unknown number: {}", number, ex);
        }
    }

    public FeatureGetMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_FEATURE_GET";
    }

    public int getNumber() {
        return ByteUtils.getInt(getData()[0]);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { FeatureResponse.TYPE, FeatureNotAvailableResponse.TYPE };
    }
}
