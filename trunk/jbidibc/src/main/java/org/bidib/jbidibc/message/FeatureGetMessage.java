package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.FeatureEnum;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command to get feature settings of a specific feature
 */
public class FeatureGetMessage extends BidibMessage {
    private static final Logger LOGGER = LoggerFactory.getLogger(FeatureGetMessage.class);

    public FeatureGetMessage(int number) {
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

    public int getNumber() {
        return ByteUtils.getInt(getData()[0]);
    }
}
