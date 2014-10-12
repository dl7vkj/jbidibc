package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.LcConfig;
import org.bidib.jbidibc.core.enumeration.LcOutputType;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.message.BidibMessage;
import org.bidib.jbidibc.core.message.LcConfigResponse;
import org.bidib.jbidibc.core.message.ResponseFactory;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LcConfigResponseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LcConfigResponseTest.class);

    // 23.03.2014 17:22:25.986: receive LcConfigResponse[[2],num=171,type=194,data=[0, 16, 2, 15, 0, 0]] : 0A 02 00 AB
    // C2 00 10 02 0F 00 00

    @Test
    public void createLcConfigResponse1FromByteArray() throws ProtocolException {
        byte[] message = { 0x0a, 0x02, 0x00, (byte) 0xAB, (byte) 0xc2, 0x00, 0x10, 0x02, 0x0F, 0x00, 0x00 };

        BidibMessage bidibMessage = ResponseFactory.create(message);
        Assert.assertNotNull(bidibMessage);
        Assert.assertEquals(ByteUtils.getInt(bidibMessage.getType()), BidibLibrary.MSG_LC_CONFIG);

        LcConfigResponse lcConfigResponse = (LcConfigResponse) bidibMessage;
        LOGGER.info("lcConfigResponse: {}", lcConfigResponse);

        LcConfig lcConfig = lcConfigResponse.getLcConfig();
        Assert.assertNotNull(lcConfig);
        LOGGER.info("lcConfig: {}", lcConfig);

        Assert.assertEquals(lcConfig.getOutputNumber(), 16);
        Assert.assertEquals(lcConfig.getOutputType(), LcOutputType.SWITCHPORT);
        Assert.assertFalse(lcConfig.isInActive());

        Assert.assertEquals(lcConfig.getValue1(), 2);
        Assert.assertEquals(lcConfig.getValue2(), 15);
    }

    @Test
    public void createLcConfigResponseInactivePortFromByteArray() throws ProtocolException {
        byte[] message =
            { 0x0a, 0x02, 0x00, (byte) 0xAB, (byte) 0xc2, (byte) 0x80 /* inactive */, 0x10, 0x02, 0x0F, 0x00, 0x00 };

        BidibMessage bidibMessage = ResponseFactory.create(message);
        Assert.assertNotNull(bidibMessage);
        Assert.assertEquals(ByteUtils.getInt(bidibMessage.getType()), BidibLibrary.MSG_LC_CONFIG);

        LcConfigResponse lcConfigResponse = (LcConfigResponse) bidibMessage;
        LOGGER.info("lcConfigResponse: {}", lcConfigResponse);

        LcConfig lcConfig = lcConfigResponse.getLcConfig();
        Assert.assertNotNull(lcConfig);
        LOGGER.info("lcConfig: {}", lcConfig);

        Assert.assertEquals(lcConfig.getOutputNumber(), 16);
        Assert.assertEquals(lcConfig.getOutputType(), LcOutputType.SWITCHPORT);
        Assert.assertTrue(lcConfig.isInActive());

        Assert.assertEquals(lcConfig.getValue1(), 2);
        Assert.assertEquals(lcConfig.getValue2(), 15);
    }
}
