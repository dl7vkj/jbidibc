package org.bidib.jbidibc.enumeration;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FeatureEnumTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(FeatureEnumTest.class);
	@Test
	public void createFeatureBM_CV_ON() {
		Feature feature = new Feature(BidibLibrary.FEATURE_BM_CV_ON, 1);
		
		LOGGER.info("Created feature with name: {}", feature.getFeatureName());
		Assert.assertEquals(feature.getFeatureName(), FeatureEnum.FEATURE_BM_CV_ON);
	}

	@Test(expectedExceptions=IllegalArgumentException.class)
	public void createInvalidFeature() {
		Feature feature = new Feature(-1, 1);
		
		feature.getFeatureName();
		
		Assert.fail();
	}
}
