package org.bidib.jbidibc.enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LcOutputTypeTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LcOutputTypeTest.class);

    @Test
    public void valueOf() {
        Assert.assertEquals(LcOutputType.valueOf((byte) 0x00), LcOutputType.SWITCHPORT);

        Assert.assertEquals(LcOutputType.valueOf((byte) 0x01), LcOutputType.LIGHTPORT);

        Assert.assertEquals(LcOutputType.valueOf((byte) 0x06), LcOutputType.BACKLIGHTPORT);

        Assert.assertEquals(LcOutputType.valueOf((byte) 240), LcOutputType.SERVOMOVE_QUERY);
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void valueOfFailure() {
        try {
            LcOutputType.valueOf((byte) 0x1F);
        }
        catch (IllegalArgumentException ex) {
            LOGGER.warn("Parse LcOutputType failed.", ex);
            throw ex;
        }
    }
}
