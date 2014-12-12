package org.bidib.jbidibc.core.message;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bidib.jbidibc.core.LcConfigX;
import org.bidib.jbidibc.core.enumeration.LcOutputType;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.bidib.jbidibc.core.utils.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LcConfigXSetMessageTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LcConfigXSetMessageTest.class);

    @Test
    public void getLcConfigX() {

        Map<Byte, Number> values = new LinkedHashMap<>();
        values.put((byte) 1, (byte) 32);
        values.put((byte) 2, (byte) 2);
        // 0x82 is assumed to be a real 32-bit value
        values.put((byte) 0x82, Integer.valueOf(16702650));
        LcConfigX lcConfigX = new LcConfigX(LcOutputType.LIGHTPORT, 1, values);

        LcConfigXSetMessage lcConfigXSetMessage = new LcConfigXSetMessage(lcConfigX);

        LcConfigX lcConfigX2 = lcConfigXSetMessage.getLcConfigX();
        Assert.assertNotNull(lcConfigX2);

        byte[] codedPortConfig = MessageUtils.getCodedPortConfig(lcConfigX2);
        Assert.assertNotNull(codedPortConfig);
        LOGGER.info("Coded port config: {}", ByteUtils.bytesToHex(codedPortConfig));

        Assert.assertEquals(codedPortConfig, new byte[] { 0x01, 0x01, 0x01, 0x20, 0x02, 0x02, (byte) 0x82, (byte) 0xBA,
            (byte) 0xDC, (byte) 0xFE, 0x00 });
    }
}
