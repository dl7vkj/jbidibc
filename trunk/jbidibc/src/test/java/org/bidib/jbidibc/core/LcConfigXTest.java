package org.bidib.jbidibc.core;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bidib.jbidibc.core.enumeration.LcOutputType;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.bidib.jbidibc.core.utils.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LcConfigXTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LcConfigXTest.class);

    @Test
    public void getPortConfig() {
        Map<Byte, Number> values = new LinkedHashMap<>();
        values.put((byte) 1, (byte) 32);
        values.put((byte) 2, (byte) 2);
        values.put((byte) 0x81, Integer.valueOf(16702650));
        LcConfigX lcConfigX = new LcConfigX(LcOutputType.LIGHTPORT, 1, values);

        byte[] codedPortConfig = MessageUtils.getCodedPortConfig(lcConfigX);
        Assert.assertNotNull(codedPortConfig);
        LOGGER.info("Coded port config: {}", ByteUtils.bytesToHex(codedPortConfig));

        Assert.assertEquals(MessageUtils.getCodedPortConfig(lcConfigX), new byte[] { 0x01, 0x01, 0x01, 0x20, 0x02,
            0x02, (byte) 0x81, (byte) 0xBA, (byte) 0xDC, (byte) 0xFE, 0x00 });
    }
}
