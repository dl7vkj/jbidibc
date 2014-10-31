package org.bidib.jbidibc.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LogarithmicJSliderTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogarithmicJSliderTest.class);

    @Test
    public void LogarithmicJSlider() {
        LogarithmicJSlider logarithmicJSlider = new LogarithmicJSlider(1, 255, 1);

        Assert.assertNotNull(logarithmicJSlider.getUI());

        if (OS.isWindows()) {
            LOGGER.info("Current OS is windows.");
            Assert.assertEquals(logarithmicJSlider.getUI().getClass().getName(), LogarithmicJSlider.UI_CLASS_WINDOWS);
        }
        else {
            LOGGER.info("Current OS is not windows.");
            Assert.assertEquals(logarithmicJSlider.getUI().getClass().getName(), LogarithmicJSlider.UI_CLASS_DEFAULT);
        }
    }
}
