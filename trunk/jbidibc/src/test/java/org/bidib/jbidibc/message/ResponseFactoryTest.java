package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ResponseFactoryTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseFactoryTest.class);

    @Test
    public void createValidSysMagicResponseMessage() throws ProtocolException {
        byte[] message = { 0x05, 0x00, 0x00, (byte) 0x81, (byte) 0xFE, (byte) 0xAF };

        BidibMessage bidibMessage = ResponseFactory.create(message);

        Assert.assertNotNull(bidibMessage);
        Assert.assertTrue(bidibMessage instanceof SysMagicResponse, "Expected a SysMagicResponse message.");
    }

    @Test(expectedExceptions = ProtocolException.class, expectedExceptionsMessageRegExp = "no magic received")
    public void createInvalidSysMagicResponseMessage() throws ProtocolException {
        byte[] message = { 0x05, 0x00, 0x00, (byte) 0x81, (byte) 0xFE, (byte) 0xAD };

        ResponseFactory.create(message);

        Assert.fail("Should have thrown an exception!");
    }

    @Test
    public void createValidNodeTabCountResponseMessage() throws ProtocolException {
        byte[] message = { 0x04, 0x00, 0x01, (byte) 0x88, 0x01 };

        BidibMessage bidibMessage = ResponseFactory.create(message);

        Assert.assertNotNull(bidibMessage);
        Assert.assertTrue(bidibMessage instanceof NodeTabCountResponse, "Expected a NodeTabCountResponse message.");
    }

    @Test
    public void createValidNodeTabResponseMessage() throws ProtocolException {
        byte[] message = { 0x0c, 0x00, 0x02, (byte) 0x89, 0x01, 0x00, (byte) 0xc0, 0x00, 0x0d, 0x68, 0x00, 0x01, 0x00 };

        BidibMessage bidibMessage = ResponseFactory.create(message);

        Assert.assertNotNull(bidibMessage);
        Assert.assertTrue(bidibMessage instanceof NodeTabResponse, "Expected a NodeTabResponse message.");
    }

    @Test
    public void createValidFeatureResponseMessage() throws ProtocolException {
        byte[] message = { 0x05, 0x00, 0x01, (byte) 0x90, 0x00, 0x10 };

        BidibMessage bidibMessage = ResponseFactory.create(message);

        Assert.assertNotNull(bidibMessage);
        Assert.assertTrue(bidibMessage instanceof FeatureResponse, "Expected a FeatureResponse message.");
    }

    @Test
    public void createValidFeatureNotAvailableResponseMessage() throws ProtocolException {
        byte[] message = { 0x04, 0x00, 0x04, (byte) 0x91, (byte) 0xfd, (byte) 0xde };

        BidibMessage bidibMessage = ResponseFactory.create(message);

        Assert.assertNotNull(bidibMessage);
        Assert.assertTrue(bidibMessage instanceof FeatureNotAvailableResponse,
            "Expected a FeatureNotAvailableResponse message.");
    }

    @Test(expectedExceptions = ProtocolException.class, expectedExceptionsMessageRegExp = "got unknown response with type 223")
    public void createUndefinedResponseMessage() throws ProtocolException {
        byte[] message = { 0x04, 0x00, 0x01, (byte) 0xDF, 0x01 };

        ResponseFactory.create(message);

        Assert.fail("Should have thrown an exception!");
    }

    // 04 00 04 B1 2C
    @Test
    public void createValidBoostCurrentResponseMessage() throws ProtocolException {
        byte[] message = { 0x04, 0x00, 0x04, (byte) 0xB1, (byte) 0x2c };

        BidibMessage bidibMessage = ResponseFactory.create(message);

        Assert.assertNotNull(bidibMessage);
        Assert.assertTrue(bidibMessage instanceof BoostCurrentResponse, "Expected a BoostCurrentResponse message.");

        LOGGER.info("Booster current: {}", ((BoostCurrentResponse) bidibMessage).getCurrent());

        // 04 00 6A B1 47

        byte[] message2 = { 0x04, 0x00, 0x6A, (byte) 0xB1, (byte) 0x47 };

        bidibMessage = ResponseFactory.create(message2);

        Assert.assertNotNull(bidibMessage);
        Assert.assertTrue(bidibMessage instanceof BoostCurrentResponse, "Expected a BoostCurrentResponse message.");

        LOGGER.info("Booster current: {}", ((BoostCurrentResponse) bidibMessage).getCurrent());

        // 04 00 60 B1 45
        byte[] message3 = { 0x04, 0x00, 0x60, (byte) 0xB1, (byte) 0x45 };

        bidibMessage = ResponseFactory.create(message3);

        Assert.assertNotNull(bidibMessage);
        Assert.assertTrue(bidibMessage instanceof BoostCurrentResponse, "Expected a BoostCurrentResponse message.");

        LOGGER.info("Booster current: {}", ((BoostCurrentResponse) bidibMessage).getCurrent());

    }

    @Test
    public void createValidFeedbackMultipleResponse() throws ProtocolException {
        byte[] message = { 0x07, 0x00, 0x01, (byte) 0xA2, 0x01, (byte) 0x80, 0x03, 0x04 };
        BidibMessage bidibMessage = ResponseFactory.create(message);

        Assert.assertEquals(((FeedbackMultipleResponse) bidibMessage).getSize(), 128);
    }
    
    @Test
    public void createMessageFeedbackMultipleResponseFromByteArray() throws ProtocolException {
        byte[] message = { 0x16, 0x01, 0x00, (byte) 0x86, (byte) 0xA2, 0x00, (byte) 0x80, (byte) 0xff, (byte) 0xff, 
            (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 
            (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff };

        BidibMessage bidibMessage = ResponseFactory.create(message);
        Assert.assertEquals(bidibMessage.getType(), (byte) BidibLibrary.MSG_BM_MULTIPLE);
        
        Assert.assertEquals(((FeedbackMultipleResponse)bidibMessage).getSize(), 0x80);
    }
    
}
