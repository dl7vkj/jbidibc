package org.bidib.jbidibc.core.message;

import java.util.Map;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.LcConfigX;
import org.bidib.jbidibc.core.enumeration.LcOutputType;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LcConfigXResponseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LcConfigXResponseTest.class);

    @Test
    public void getLcConfigX() throws ProtocolException {

        byte[] message =
            { 0x0e, 0x02, 0x00, (byte) 0xAB, (byte) 0xc6, 0x00, 0x10, 0x01, (byte) 0xFF, 0x02, 0x00, 0x03, 0x06, 0x04,
                0x05 };

        BidibMessage bidibMessage = ResponseFactory.create(message);
        Assert.assertNotNull(bidibMessage);
        Assert.assertEquals(ByteUtils.getInt(bidibMessage.getType()), BidibLibrary.MSG_LC_CONFIGX);

        LcConfigXResponse lcConfigXResponse = (LcConfigXResponse) bidibMessage;
        LOGGER.info("lcConfigXResponse: {}", lcConfigXResponse);

        LcConfigX lcConfigX = lcConfigXResponse.getLcConfigX();
        Assert.assertNotNull(lcConfigX);
        LOGGER.info("lcConfigX: {}", lcConfigX);

        Assert.assertEquals(lcConfigX.getOutputNumber(), 16);
        Assert.assertEquals(lcConfigX.getOutputType(), LcOutputType.SWITCHPORT);

        Map<Byte, Number> portConfig = lcConfigX.getPortConfig();
        Assert.assertNotNull(portConfig);

        Assert.assertEquals(portConfig.get(Byte.valueOf((byte) 1)).byteValue() & 0xFF, 0xFF);
        Assert.assertEquals(portConfig.get(Byte.valueOf((byte) 2)).byteValue() & 0xFF, 0x00);
        Assert.assertEquals(portConfig.get(Byte.valueOf((byte) 3)).byteValue() & 0xFF, 0x06);
        Assert.assertEquals(portConfig.get(Byte.valueOf((byte) 4)).byteValue() & 0xFF, 0x05);
    }

    @Test
    public void getLcConfigXWithInt() throws ProtocolException {

        byte[] message =
            { 0x11, 0x02, 0x00, (byte) 0xAB, (byte) 0xc6, 0x00, 0x10, 0x01, (byte) 0xFF, 0x02, 0x00, 0x03, 0x06,
                (byte) 0x81, (byte) 0xBA, (byte) 0xDC, (byte) 0xFE, 0x00 };

        BidibMessage bidibMessage = ResponseFactory.create(message);
        Assert.assertNotNull(bidibMessage);
        Assert.assertEquals(ByteUtils.getInt(bidibMessage.getType()), BidibLibrary.MSG_LC_CONFIGX);

        LcConfigXResponse lcConfigXResponse = (LcConfigXResponse) bidibMessage;
        LOGGER.info("lcConfigXResponse: {}", lcConfigXResponse);

        LcConfigX lcConfigX = lcConfigXResponse.getLcConfigX();
        Assert.assertNotNull(lcConfigX);
        LOGGER.info("lcConfigX: {}", lcConfigX);

        Assert.assertEquals(lcConfigX.getOutputNumber(), 16);
        Assert.assertEquals(lcConfigX.getOutputType(), LcOutputType.SWITCHPORT);

        Map<Byte, Number> portConfig = lcConfigX.getPortConfig();
        Assert.assertNotNull(portConfig);

        Assert.assertEquals(portConfig.get(Byte.valueOf((byte) 1)).byteValue() & 0xFF, 0xFF);
        Assert.assertEquals(portConfig.get(Byte.valueOf((byte) 2)).byteValue() & 0xFF, 0x00);
        Assert.assertEquals(portConfig.get(Byte.valueOf((byte) 3)).byteValue() & 0xFF, 0x06);
        Assert.assertEquals(portConfig.get(Byte.valueOf((byte) 0x81)).intValue() & 0xFFFFFFFF, 0xFEDCBA);
    }

    @Test
    public void getLcConfigXReconfigLightPort() throws ProtocolException {

        // !!! no ACT_TYPE !!!
        byte[] message =
            { 0x09, 0x02, 0x00, (byte) 0xAB, (byte) 0xc6, 0x01/* PORT_TYPE */, 0x02,
                (byte) BidibLibrary.BIDIB_PCFG_RECONFIG, 0x03, (byte) 0x80 };

        // BIDIB_PCFG_RECONFIG (byte) 0x45

        BidibMessage bidibMessage = ResponseFactory.create(message);
        Assert.assertNotNull(bidibMessage);
        Assert.assertEquals(ByteUtils.getInt(bidibMessage.getType()), BidibLibrary.MSG_LC_CONFIGX);

        LcConfigXResponse lcConfigXResponse = (LcConfigXResponse) bidibMessage;
        LOGGER.info("lcConfigXResponse: {}", lcConfigXResponse);

        LcConfigX lcConfigX = lcConfigXResponse.getLcConfigX();
        Assert.assertNotNull(lcConfigX);
        LOGGER.info("lcConfigX: {}", lcConfigX);

        Assert.assertEquals(lcConfigX.getOutputType(), LcOutputType.LIGHTPORT);
        Assert.assertEquals(lcConfigX.getOutputNumber(), 2);

        Map<Byte, Number> portConfig = lcConfigX.getPortConfig();
        Assert.assertNotNull(portConfig);

        Assert.assertEquals(portConfig.get(Byte.valueOf((byte) (BidibLibrary.BIDIB_PCFG_RECONFIG & 0xFF))), 0x8003);

    }

    @Test
    public void getLcConfigXReconfigServoPort() throws ProtocolException {

        // !!! no ACT_TYPE !!!
        byte[] message =
            { 0x09, 0x02, 0x00, (byte) 0xAB, (byte) 0xc6, 0x02/* OUTPUT_TYPE */, 0x02,
                (byte) BidibLibrary.BIDIB_PCFG_RECONFIG, 0x04, (byte) 0x00 };

        // BIDIB_PCFG_RECONFIG (byte) 0x45

        BidibMessage bidibMessage = ResponseFactory.create(message);
        Assert.assertNotNull(bidibMessage);
        Assert.assertEquals(ByteUtils.getInt(bidibMessage.getType()), BidibLibrary.MSG_LC_CONFIGX);

        LcConfigXResponse lcConfigXResponse = (LcConfigXResponse) bidibMessage;
        LOGGER.info("lcConfigXResponse: {}", lcConfigXResponse);

        LcConfigX lcConfigX = lcConfigXResponse.getLcConfigX();
        Assert.assertNotNull(lcConfigX);
        LOGGER.info("lcConfigX: {}", lcConfigX);

        Assert.assertEquals(lcConfigX.getOutputType(), LcOutputType.SERVOPORT);
        Assert.assertEquals(lcConfigX.getOutputNumber(), 2);

        Map<Byte, Number> portConfig = lcConfigX.getPortConfig();
        Assert.assertNotNull(portConfig);

        Assert.assertEquals(portConfig.get(Byte.valueOf((byte) (BidibLibrary.BIDIB_PCFG_RECONFIG & 0xFF))), 0x0004);

    }
}
