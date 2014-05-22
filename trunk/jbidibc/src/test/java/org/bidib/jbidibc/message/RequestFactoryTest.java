package org.bidib.jbidibc.message;

import java.util.List;

import org.bidib.jbidibc.AddressData;
import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.AddressTypeEnum;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class RequestFactoryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestFactoryTest.class);

    private RequestFactory requestFactory;

    @BeforeClass
    public void init() {
        requestFactory = new RequestFactory();
    }

    @Test
    public void createSysGetMagicTest() throws ProtocolException {
        byte[] message = new byte[] { (byte) 0xFE, 0x03, 0x00, 0x00, 0x01, (byte) 0xD6, (byte) 0xFE };
        List<BidibCommand> bidibMessages = requestFactory.create(message);
        LOGGER.info("Created messages: {}", bidibMessages);

        Assert.assertNotNull(bidibMessages);
        Assert.assertEquals(BidibLibrary.MSG_SYS_GET_MAGIC, ByteUtils.getInt(bidibMessages.get(0).getType()));
    }

    // 22:39:33.288 [INFO] org.bidib.wizard.comm.mock.SimulationBidib [AWT-EventQueue-0] - Send is called with bytes: FE
    // 06 01 00 0C 17 01 32 06 01 00 0D 17 01 33 06 01 00 0E 17 01 34 06 01 00 0F 17 01 35 D4 FE
    @Test
    public void bulkGetVendorTest() throws ProtocolException {
        byte[] message =
            new byte[] { (byte) 0xFE, 0x06, 0x01, 0x00, 0x0C, 0x17, 0x01, 0x32, 0x06, 0x01, 0x00, 0x0D, 0x17, 0x01,
                0x33, 0x06, 0x01, 0x00, 0x0E, 0x17, 0x01, 0x34, 0x06, 0x01, 0x00, 0x0F, 0x17, 0x01, 0x35, (byte) 0xD4,
                (byte) 0xFE };
        List<BidibCommand> bidibMessages = requestFactory.create(message);
        LOGGER.info("Created messages: {}", bidibMessages);

        Assert.assertNotNull(bidibMessages);
        Assert.assertEquals(BidibLibrary.MSG_VENDOR_GET, ByteUtils.getInt(bidibMessages.get(0).getType()));
    }

    @Test
    public void commandStationDriveMessageTest() throws ProtocolException {
        byte[] message =
            new byte[] { (byte) 0xFE, 0x0C, 0x00, (byte) 0xFC, 0x64, 0x62, 0x02, 0x03, 0x03, 0x22, 0x10, 0x00, 0x00,
                0x00, (byte) 0x1B, (byte) 0xFE };
        List<BidibCommand> bidibMessages = requestFactory.create(message);
        LOGGER.info("Created messages: {}", bidibMessages);

        Assert.assertNotNull(bidibMessages);
        Assert.assertEquals(BidibLibrary.MSG_CS_DRIVE, ByteUtils.getInt(bidibMessages.get(0).getType()));

        CommandStationDriveMessage commandStationDriveMessage = (CommandStationDriveMessage) bidibMessages.get(0);

        Assert.assertEquals(new AddressData(610, AddressTypeEnum.LOCOMOTIVE_BACKWARD),
            commandStationDriveMessage.getDecoderAddress());

        Assert.assertEquals(0x22, commandStationDriveMessage.getSpeed());

        message =
            new byte[] { (byte) 0xFE, 0x0C, 0x00, (byte) 0xFC, 0x64, 0x62, 0x02, 0x03, 0x03, 0x00, 0x10, 0x00, 0x00,
                0x00, (byte) 0xB5, (byte) 0xFE };
        bidibMessages = requestFactory.create(message);
        LOGGER.info("Created messages: {}", bidibMessages);

        Assert.assertNotNull(bidibMessages);
        Assert.assertEquals(BidibLibrary.MSG_CS_DRIVE, ByteUtils.getInt(bidibMessages.get(0).getType()));

        commandStationDriveMessage = (CommandStationDriveMessage) bidibMessages.get(0);

        Assert.assertEquals(new AddressData(610, AddressTypeEnum.LOCOMOTIVE_BACKWARD),
            commandStationDriveMessage.getDecoderAddress());

        Assert.assertEquals(0, commandStationDriveMessage.getSpeed());
    }
}
