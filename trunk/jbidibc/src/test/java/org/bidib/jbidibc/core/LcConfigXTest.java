package org.bidib.jbidibc.core;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bidib.jbidibc.core.enumeration.LcOutputType;
import org.bidib.jbidibc.core.port.BytePortConfigValue;
import org.bidib.jbidibc.core.port.Int32PortConfigValue;
import org.bidib.jbidibc.core.port.PortConfigValue;
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
        Map<Byte, PortConfigValue<?>> values = new LinkedHashMap<>();
        values.put((byte) 1, new BytePortConfigValue((byte) 32));
        values.put((byte) 2, new BytePortConfigValue((byte) 2));
        // 0x82 is assumed to be a real 32-bit value
        values.put((byte) 0x82, new Int32PortConfigValue(Integer.valueOf(16702650)));
        LcConfigX lcConfigX = new LcConfigX(LcOutputType.LIGHTPORT, 1, values);

        byte[] codedPortConfig = MessageUtils.getCodedPortConfig(lcConfigX);
        Assert.assertNotNull(codedPortConfig);
        LOGGER.info("Coded port config: {}", ByteUtils.bytesToHex(codedPortConfig));

        Assert.assertEquals(MessageUtils.getCodedPortConfig(lcConfigX), new byte[] { 0x01, 0x01, 0x01, 0x20, 0x02,
            0x02, (byte) 0x82, (byte) 0xBA, (byte) 0xDC, (byte) 0xFE, 0x00 });
    }
}
