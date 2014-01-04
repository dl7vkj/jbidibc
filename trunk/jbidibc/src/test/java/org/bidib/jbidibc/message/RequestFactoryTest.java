package org.bidib.jbidibc.message;

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
    public void create() throws ProtocolException {
        byte[] message = new byte[] { (byte) 0xFE, 0x03, 0x00, 0x00, 0x01, (byte) 0xD6, (byte) 0xFE };
        BidibMessage bidibMessage = RequestFactory.create(message);
        LOGGER.info("Created message: {}", message);

        Assert.assertNotNull(bidibMessage);
        Assert.assertEquals(BidibLibrary.MSG_SYS_GET_MAGIC, ByteUtils.getInt(bidibMessage.getType()));
    }
}
