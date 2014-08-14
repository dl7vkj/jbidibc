package org.bidib.jbidibc.message;

import org.bidib.jbidibc.enumeration.AddressTypeEnum;
import org.bidib.jbidibc.exception.ProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
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

        Assert.assertNotNull(feedbackCvResponse.getAddress());
        Assert.assertEquals(feedbackCvResponse.getAddress().getAddress(), 3);
        Assert.assertEquals(feedbackCvResponse.getAddress().getType(), AddressTypeEnum.LOCOMOTIVE_FORWARD);
        Assert.assertEquals(feedbackCvResponse.getCvNumber(), 5);
        Assert.assertEquals(feedbackCvResponse.getDat(), 1);
    }

    @Test
    public void shortAddressInCV29Test() throws ProtocolException {
        byte[] message = new byte[] { 0x08, 0x00, 0x1A, (byte) 0xA5, 0x03, 0x00, 0x1C, 0x00, 0x0E };

        BidibMessage result = new BidibMessage(message);
        FeedbackCvResponse feedbackCvResponse =
            new FeedbackCvResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());

        Assert.assertNotNull(feedbackCvResponse);
        LOGGER.info("Prepared feedbackCvResponse: {}", feedbackCvResponse);

        Assert.assertNotNull(feedbackCvResponse.getAddress());
        Assert.assertEquals(feedbackCvResponse.getAddress().getAddress(), 3);
        Assert.assertEquals(feedbackCvResponse.getAddress().getType(), AddressTypeEnum.LOCOMOTIVE_FORWARD);
        Assert.assertEquals(feedbackCvResponse.getCvNumber(), 29);
        Assert.assertEquals(feedbackCvResponse.getDat(), 14);

        // check for bit 5 --> must be reset
        int bitMask = prepareCompareBitMask(5);
        int cvValue = feedbackCvResponse.getDat();
        int res = cvValue & bitMask;

        Assert.assertEquals(res, 0);
    }

    @Test
    public void longAddressInCV29Test() throws ProtocolException {
        byte[] message = new byte[] { 0x08, 0x00, 0x1A, (byte) 0xA5, 0x03, 0x00, 0x1C, 0x00, 0x2E };

        BidibMessage result = new BidibMessage(message);
        FeedbackCvResponse feedbackCvResponse =
            new FeedbackCvResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());

        Assert.assertNotNull(feedbackCvResponse);
        LOGGER.info("Prepared feedbackCvResponse: {}", feedbackCvResponse);

        Assert.assertNotNull(feedbackCvResponse.getAddress());
        Assert.assertEquals(feedbackCvResponse.getAddress().getAddress(), 3);
        Assert.assertEquals(feedbackCvResponse.getAddress().getType(), AddressTypeEnum.LOCOMOTIVE_FORWARD);
        Assert.assertEquals(feedbackCvResponse.getCvNumber(), 29);
        Assert.assertEquals(feedbackCvResponse.getDat(), 46);

        // check for bit 5 --> must be set
        int bitMask = prepareCompareBitMask(5);
        int cvValue = feedbackCvResponse.getDat();
        int res = cvValue & bitMask;

        Assert.assertEquals(res, bitMask);
    }

    protected int prepareCompareBitMask(int bitNumber) {
        int compareValue = (1 << bitNumber);
        return compareValue;
    }

    // 08 00 45 A5 03 00 02 00 04
    @Test
    public void prepareCVFeedbackTest() throws ProtocolException {
        byte[] message = new byte[] { 0x08, 0x00, 0x45, (byte) 0xA5, 0x03, 0x00, 0x02, 0x00, 0x04 };

        BidibMessage result = new BidibMessage(message);
        FeedbackCvResponse feedbackCvResponse =
            new FeedbackCvResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());

        Assert.assertNotNull(feedbackCvResponse);
        LOGGER.info("Prepared feedbackCvResponse: {}", feedbackCvResponse);

        Assert.assertNotNull(feedbackCvResponse.getAddress());
        Assert.assertEquals(feedbackCvResponse.getAddress().getAddress(), 3);
        Assert.assertEquals(feedbackCvResponse.getAddress().getType(), AddressTypeEnum.LOCOMOTIVE_FORWARD);
        Assert.assertEquals(feedbackCvResponse.getCvNumber(), 3);
        Assert.assertEquals(feedbackCvResponse.getDat(), 4);
    }

    // IN --- : MSG_BM_CV 09 01 00 19 A5 03 00 03 00 05
    @Test
    public void tamsCVFeedbackTest() throws ProtocolException {
        byte[] message = new byte[] { 0x09, 0x01, 0x00, 0x19, (byte) 0xA5, 0x03, 0x00, 0x03, 0x00, 0x05 };

        BidibMessage result = new BidibMessage(message);
        FeedbackCvResponse feedbackCvResponse =
            new FeedbackCvResponse(result.getAddr(), result.getNum(), result.getType(), result.getData());

        Assert.assertNotNull(feedbackCvResponse);
        LOGGER.info("Prepared feedbackCvResponse: {}", feedbackCvResponse);

        Assert.assertNotNull(feedbackCvResponse.getAddress());
        Assert.assertEquals(feedbackCvResponse.getAddress().getAddress(), 3);
        Assert.assertEquals(feedbackCvResponse.getAddress().getType(), AddressTypeEnum.LOCOMOTIVE_FORWARD);
        Assert.assertEquals(feedbackCvResponse.getCvNumber(), 4);
        Assert.assertEquals(feedbackCvResponse.getDat(), 5);
    }
}
