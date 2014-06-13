package org.bidib.jbidibc.enumeration;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LcOutputTypeTest {

    @Test
    public void valueOf() {
        Assert.assertEquals(LcOutputType.valueOf((byte) 0x00), LcOutputType.SWITCHPORT);
        Assert.assertEquals(LcOutputType.valueOf((byte) 0x80), LcOutputType.SWITCHPORT);

        Assert.assertEquals(LcOutputType.valueOf((byte) 0x01), LcOutputType.LIGHTPORT);
        Assert.assertEquals(LcOutputType.valueOf((byte) 0x81), LcOutputType.LIGHTPORT);

        Assert.assertEquals(LcOutputType.valueOf((byte) 0x06), LcOutputType.BACKLIGHTPORT);
        Assert.assertEquals(LcOutputType.valueOf((byte) 0x86), LcOutputType.BACKLIGHTPORT);
    }
}
