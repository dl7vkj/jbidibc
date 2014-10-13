package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.LcPortMapping;
import org.bidib.jbidibc.core.enumeration.LcMappingPortType;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.message.BidibMessage;
import org.bidib.jbidibc.core.message.LcMappingResponse;
import org.bidib.jbidibc.core.message.ResponseFactory;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LcMappingResponseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LcMappingResponseTest.class);

    @Test
    public void getPortAvailable16Ports() throws ProtocolException {
        byte[] message = { 0x0a, 0x01, 0x00, (byte) 0xd5, (byte) 0xc5, 0x00, 0x10, 0x01, 0x06, 0x02, 0x00 };

        BidibMessage bidibMessage = ResponseFactory.create(message);
        Assert.assertNotNull(bidibMessage);
        Assert.assertEquals(ByteUtils.getInt(bidibMessage.getType()), BidibLibrary.MSG_LC_MAPPING);

        LcMappingResponse lcMappingResponse = (LcMappingResponse) bidibMessage;
        LOGGER.info("lcMacroResponse: {}", lcMappingResponse);

        Assert.assertEquals(lcMappingResponse.getLcMappingPortType(), LcMappingPortType.SWITCHPORT);
        Assert.assertEquals(lcMappingResponse.getPortCount(), 16);

        Assert.assertEquals(lcMappingResponse.getPortAvailable(), new int[] { 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, /**/0x00, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00 });
        Assert.assertEquals(lcMappingResponse.getPortMapping(), new int[] { 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, /**/0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 });
    }

    @Test
    public void getPortAvailable1Port() throws ProtocolException {
        byte[] message = { 0x08, 0x01, 0x00, (byte) 0xd5, (byte) 0xc5, 0x00, 0x01, 0x01, 0x02 };

        BidibMessage bidibMessage = ResponseFactory.create(message);
        Assert.assertNotNull(bidibMessage);
        Assert.assertEquals(ByteUtils.getInt(bidibMessage.getType()), BidibLibrary.MSG_LC_MAPPING);

        LcMappingResponse lcMappingResponse = (LcMappingResponse) bidibMessage;
        LOGGER.info("lcMacroResponse: {}", lcMappingResponse);

        Assert.assertEquals(lcMappingResponse.getLcMappingPortType(), LcMappingPortType.SWITCHPORT);
        Assert.assertEquals(lcMappingResponse.getPortCount(), 1);

        Assert.assertEquals(lcMappingResponse.getPortAvailable(), new int[] { 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00 });
        Assert.assertEquals(lcMappingResponse.getPortMapping(), new int[] { 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00 });
    }

    @Test
    public void getLcPortMapping16Ports() throws ProtocolException {
        byte[] message = { 0x0a, 0x01, 0x00, (byte) 0xd5, (byte) 0xc5, 0x00, 0x10, 0x01, 0x06, 0x02, 0x00 };

        BidibMessage bidibMessage = ResponseFactory.create(message);
        Assert.assertNotNull(bidibMessage);
        Assert.assertEquals(ByteUtils.getInt(bidibMessage.getType()), BidibLibrary.MSG_LC_MAPPING);

        LcMappingResponse lcMappingResponse = (LcMappingResponse) bidibMessage;
        LOGGER.info("lcMacroResponse: {}", lcMappingResponse);

        Assert.assertNotNull(lcMappingResponse.getLcPortMapping());
        LcPortMapping lcPortMapping = lcMappingResponse.getLcPortMapping();

        Assert.assertEquals(lcPortMapping.getLcMappingPortType(), LcMappingPortType.SWITCHPORT);
        Assert.assertEquals(lcPortMapping.getPortCount(), 16);

        Assert.assertEquals(lcPortMapping.getPortAvailable(), new int[] { 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, /**/0x00, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00 });
        Assert.assertEquals(lcPortMapping.getPortMapping(), new int[] { 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, /**/
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 });
    }

    @Test
    public void getLcPortMapping0Ports() throws ProtocolException {
        byte[] message = { 0x06, 0x01, 0x00, (byte) 0xd5, (byte) 0xc5, 0x01, 0x00 };

        BidibMessage bidibMessage = ResponseFactory.create(message);
        Assert.assertNotNull(bidibMessage);
        Assert.assertEquals(ByteUtils.getInt(bidibMessage.getType()), BidibLibrary.MSG_LC_MAPPING);

        LcMappingResponse lcMappingResponse = (LcMappingResponse) bidibMessage;
        LOGGER.info("lcMacroResponse: {}", lcMappingResponse);

        Assert.assertNotNull(lcMappingResponse.getLcPortMapping());
        LcPortMapping lcPortMapping = lcMappingResponse.getLcPortMapping();

        Assert.assertEquals(lcPortMapping.getLcMappingPortType(), LcMappingPortType.LIGHTPORT);
        Assert.assertEquals(lcPortMapping.getPortCount(), 0);

        Assert.assertNull(lcPortMapping.getPortAvailable());
        Assert.assertNull(lcPortMapping.getPortMapping());
    }

    @Test
    public void getLcPortMappingOneControlSwitchPortsPorts() throws ProtocolException {
        int totalPorts = 40;
        byte[] message =
            { 0x10, 0x01, 0x00, (byte) 0xd5, (byte) 0xc5, 0x00, (byte) totalPorts, (byte) 0xFF, (byte) 0xFF,
                (byte) 0xFF, (byte) 0xFF, 0x00, /**/(byte) 0xE7, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x00 };

        BidibMessage bidibMessage = ResponseFactory.create(message);
        Assert.assertNotNull(bidibMessage);
        Assert.assertEquals(ByteUtils.getInt(bidibMessage.getType()), BidibLibrary.MSG_LC_MAPPING);

        LcMappingResponse lcMappingResponse = (LcMappingResponse) bidibMessage;
        LOGGER.info("lcMacroResponse: {}", lcMappingResponse);

        Assert.assertNotNull(lcMappingResponse.getLcPortMapping());
        LcPortMapping lcPortMapping = lcMappingResponse.getLcPortMapping();

        Assert.assertEquals(lcPortMapping.getLcMappingPortType(), LcMappingPortType.SWITCHPORT);
        Assert.assertEquals(lcPortMapping.getPortCount(), totalPorts);

        Assert.assertEquals(lcPortMapping.getPortAvailable(), new int[] { 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01,
            0x01, /**/0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, /**/0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01,
            0x01, /**/0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, /**/0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00 });
        Assert.assertEquals(lcPortMapping.getPortMapping(), new int[] { 0x01, 0x01, 0x01, 0x00, 0x00, 0x01, 0x01, 0x01, /**/
        0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, /**/0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, /**/
        0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, /**/0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 });
    }

    @Test
    public void getLcPortMappingOneControlInputPortsPorts() throws ProtocolException {
        int totalPorts = 40;
        byte[] message =
            { 0x10, 0x01, 0x00, (byte) 0xd5, (byte) 0xc5, BidibLibrary.BIDIB_PORTTYPE_INPUT, (byte) totalPorts,
                (byte) 0xFF, (byte) 0xFF, (byte) 0x00, (byte) 0x00, 0x00, /**/(byte) 0x18, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, 0x00 };

        BidibMessage bidibMessage = ResponseFactory.create(message);
        Assert.assertNotNull(bidibMessage);
        Assert.assertEquals(ByteUtils.getInt(bidibMessage.getType()), BidibLibrary.MSG_LC_MAPPING);

        LcMappingResponse lcMappingResponse = (LcMappingResponse) bidibMessage;
        LOGGER.info("lcMacroResponse: {}", lcMappingResponse);

        Assert.assertNotNull(lcMappingResponse.getLcPortMapping());
        LcPortMapping lcPortMapping = lcMappingResponse.getLcPortMapping();

        Assert.assertEquals(lcPortMapping.getLcMappingPortType(), LcMappingPortType.INPUTPORT);
        Assert.assertEquals(lcPortMapping.getPortCount(), totalPorts);

        Assert.assertEquals(lcPortMapping.getPortAvailable(), new int[] { 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01,
            0x01, /**/0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, /**/0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, /**/0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, /**/0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00 });
        Assert.assertEquals(lcPortMapping.getPortMapping(), new int[] { 0x00, 0x00, 0x00, 0x01, 0x01, 0x00, 0x00, 0x00, /**/
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, /**/0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, /**/
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, /**/0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 });
    }

    @Test
    public void getLcPortMappingOneControlServoPortsPorts() throws ProtocolException {
        int totalPorts = 40;
        byte[] message =
            { 0x10, 0x01, 0x00, (byte) 0xd5, (byte) 0xc5, BidibLibrary.BIDIB_OUTTYPE_SERVO, (byte) totalPorts,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFF, /**/(byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0xFF };

        BidibMessage bidibMessage = ResponseFactory.create(message);
        Assert.assertNotNull(bidibMessage);
        Assert.assertEquals(ByteUtils.getInt(bidibMessage.getType()), BidibLibrary.MSG_LC_MAPPING);

        LcMappingResponse lcMappingResponse = (LcMappingResponse) bidibMessage;
        LOGGER.info("lcMacroResponse: {}", lcMappingResponse);

        Assert.assertNotNull(lcMappingResponse.getLcPortMapping());
        LcPortMapping lcPortMapping = lcMappingResponse.getLcPortMapping();

        Assert.assertEquals(lcPortMapping.getLcMappingPortType(), LcMappingPortType.SERVOPORT);
        Assert.assertEquals(lcPortMapping.getPortCount(), totalPorts);

        Assert.assertEquals(lcPortMapping.getPortAvailable(), new int[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, /**/0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, /**/0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, /**/0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, /**/0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01,
            0x01 });
        Assert.assertEquals(lcPortMapping.getPortMapping(), new int[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, /**/
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, /**/0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, /**/
        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, /**/0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01 });
    }
}
