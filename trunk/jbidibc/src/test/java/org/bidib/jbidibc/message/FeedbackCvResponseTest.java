package org.bidib.jbidibc.message;

import org.bidib.jbidibc.exception.ProtocolException;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class FeedbackCvResponseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackCvResponseTest.class);

    // [20140316200623072] IN --- : MSG_BM_CV 08 00 D9 A5 03 00 04 00 01
    @Test
    public void getCvNumber() throws ProtocolException {
        byte[] message = new byte[] { 0x08, 0x00, (byte) 0xD9, (byte) 0xA5, 0x03, 0x00, 0x04, 0x00, 0x01 };
        BidibMessage result = new BidibMessage(message);
        FeedbackCvResponse feedbackCvResponse =
            new FeedbackCvResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());

        Assert.assertNotNull(feedbackCvResponse);
        LOGGER.info("Prepared feedbackCvResponse: {}", feedbackCvResponse);

        Assert.assertEquals(feedbackCvResponse.getAddress(), 3);
        Assert.assertEquals(feedbackCvResponse.getCvNumber(), 5);
        Assert.assertEquals(feedbackCvResponse.getDat(), 1);
    }
}
