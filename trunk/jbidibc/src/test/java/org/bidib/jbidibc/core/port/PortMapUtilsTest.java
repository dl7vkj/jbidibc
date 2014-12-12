package org.bidib.jbidibc.core.port;

import org.bidib.jbidibc.core.enumeration.LcOutputType;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PortMapUtilsTest {

    @Test
    public void supportsPortType() {

        ReconfigPortConfigValue reconfigPortConfigValue = new ReconfigPortConfigValue(0x800301);
        Assert.assertTrue(PortMapUtils.supportsPortType(LcOutputType.SWITCHPORT, reconfigPortConfigValue));
        Assert.assertTrue(PortMapUtils.supportsPortType(LcOutputType.LIGHTPORT, reconfigPortConfigValue));
        Assert.assertTrue(PortMapUtils.supportsPortType(LcOutputType.INPUTPORT, reconfigPortConfigValue));

        Assert.assertFalse(PortMapUtils.supportsPortType(LcOutputType.SERVOPORT, reconfigPortConfigValue));
    }
}
