package org.bidib.jbidibc.core;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bidib.jbidibc.core.enumeration.LcOutputType;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LcConfigXTest {

    @Test
    public void getPortConfig() {
        Map<Byte, Number> values = new LinkedHashMap<>();
        values.put((byte) 1, (byte) 32);
        values.put((byte) 2, (byte) 2);
        LcConfigX lcConfigX = new LcConfigX(LcOutputType.SWITCHPORT, 1, values);

        Assert.assertNotNull(lcConfigX.getCodedPortConfig());
    }
}
