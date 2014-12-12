package org.bidib.jbidibc.core.port;

import org.bidib.jbidibc.core.enumeration.LcOutputType;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ReconfigPortConfigValueTest {

    @Test
    public void getCurrentOutputType() {

        Assert.assertEquals(new ReconfigPortConfigValue(0x000401).getCurrentOutputType(), LcOutputType.LIGHTPORT);
        Assert.assertEquals(new ReconfigPortConfigValue(0x000402).getCurrentOutputType(), LcOutputType.SERVOPORT);
    }

    @Test
    public void getPortMap() {
        Assert.assertEquals(new ReconfigPortConfigValue(0x000401).getPortMap(), 0x0004);
        Assert.assertEquals(new ReconfigPortConfigValue(0x800302).getPortMap(), 0x8003);
    }
}
