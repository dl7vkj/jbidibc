package org.bidib.jbidibc.message;

import java.util.List;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class RequestFactoryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestFactoryTest.class);

    @Test
    public void createSysGetMagicTest() throws ProtocolException {
        byte[] message = new byte[] { (byte) 0xFE, 0x03, 0x00, 0x00, 0x01, (byte) 0xD6, (byte) 0xFE };
        List<BidibMessage> bidibMessages = RequestFactory.create(message);
        LOGGER.info("Created messages: {}", bidibMessages);

        Assert.assertNotNull(bidibMessages);
        Assert.assertEquals(BidibLibrary.MSG_SYS_GET_MAGIC, ByteUtils.getInt(bidibMessages.get(0).getType()));
    }

    // 22:39:33.288 [INFO] org.bidib.wizard.comm.mock.SimulationBidib [AWT-EventQueue-0] - Send is called with bytes: FE 06 01 00 0C 17 01 32 06 01 00 0D 17 01 33 06 01 00 0E 17 01 34 06 01 00 0F 17 01 35 D4 FE 
    @Test
    public void bulkGetVendorTest() throws ProtocolException {
        byte[] message =
            new byte[] { (byte) 0xFE, 0x06, 0x01, 0x00, 0x0C, 0x17, 0x01, 0x32, 0x06, 0x01, 0x00, 0x0D, 0x17, 0x01,
                0x33, 0x06, 0x01, 0x00, 0x0E, 0x17, 0x01, 0x34, 0x06, 0x01, 0x00, 0x0F, 0x17, 0x01, 0x35, (byte) 0xD4,
                (byte) 0xFE };
        List<BidibMessage> bidibMessages = RequestFactory.create(message);
        LOGGER.info("Created messages: {}", bidibMessages);

        Assert.assertNotNull(bidibMessages);
        Assert.assertEquals(BidibLibrary.MSG_VENDOR_GET, ByteUtils.getInt(bidibMessages.get(0).getType()));
    }

}
