package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.message.BidibMessage;
import org.bidib.jbidibc.core.message.FeedbackBlockCvResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FeedbackBlockCvResponseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackBlockCvResponseTest.class);

    // IN --- : MSG_BM_BLOCK_CV 0F 00 D9 A8 00 01 02 00 00 02 01 00 04 05 06 07
    @Test
    public void getCvNumber() throws ProtocolException {
        // dec vid: 0
        // decoder addr: 01 02
        // offset: 2
        // IDXL: 01, IDXH: 00
        byte[] message =
            new byte[] { 0x0F, 0x00, (byte) 0xD9, (byte) 0xA8, 0x00, 0x02, 0x01, 0x00, 0x00, 0x02, 0x01, 0x00, 0x04,
                0x05, 0x06, 0x07 };
        BidibMessage result = new BidibMessage(message);
        FeedbackBlockCvResponse feedbackBlockCvResponse =
            new FeedbackBlockCvResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());

        Assert.assertNotNull(feedbackBlockCvResponse);
        LOGGER.info("Prepared feedbackBlockCvResponse: {}", feedbackBlockCvResponse);

        Assert.assertNotNull(feedbackBlockCvResponse.getAddress());
        Assert.assertEquals(feedbackBlockCvResponse.getAddress().getAddress(), 258);
        Assert.assertEquals(feedbackBlockCvResponse.getCvNumber(), 259);
        Assert.assertEquals(feedbackBlockCvResponse.getDat(), new byte[] { 0x04, 0x05, 0x06, 0x07 });
    }

    // IN --- : MSG_BM_BLOCK_CV 0F 00 D9 A8 61 01 02 03 04 02 00 01 00 04 05 06 07
    @Test
    public void getDecoderSerialNumber() throws ProtocolException {
        // 0x61 : D&H
        // offset: 2
        byte[] message =
            new byte[] { 0x0F, 0x00, (byte) 0xD9, (byte) 0xA8, 0x61, 0x01, 0x02, 0x03, 0x04, 0x02, 0x01, 0x00, 0x04,
                0x05, 0x06, 0x07 };
        BidibMessage result = new BidibMessage(message);
        FeedbackBlockCvResponse feedbackBlockCvResponse =
            new FeedbackBlockCvResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());

        Assert.assertNotNull(feedbackBlockCvResponse);
        LOGGER.info("Prepared feedbackBlockCvResponse: {}", feedbackBlockCvResponse);

        Assert.assertNull(feedbackBlockCvResponse.getAddress());

        Assert.assertEquals(feedbackBlockCvResponse.getDecoderVendorId(), 0x61);
        Assert.assertEquals(feedbackBlockCvResponse.getDecoderSerialNum(), 0x04030201);

        Assert.assertEquals(feedbackBlockCvResponse.getCvNumber(), 259);
        Assert.assertEquals(feedbackBlockCvResponse.getDat(), new byte[] { 0x04, 0x05, 0x06, 0x07 });
    }
}
