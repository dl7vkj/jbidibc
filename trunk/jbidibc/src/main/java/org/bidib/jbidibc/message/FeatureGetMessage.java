package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.FeatureEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command to get feature settings of a specific feature
 */
public class FeatureGetMessage extends BidibMessage {
	private static final Logger LOGGER = LoggerFactory.getLogger(FeatureGetMessage.class);
	
    public FeatureGetMessage(int number) {
        super(0, BidibLibrary.MSG_FEATURE_GET, new byte[] { (byte) number });
        
        try {
        	LOGGER.debug("Prepared get feature: {}", FeatureEnum.valueOf((byte) number));
        }
        catch(IllegalArgumentException ex) {
        	LOGGER.warn("Create feature request with unknown number: {}", number, ex);
        }
    }
}
