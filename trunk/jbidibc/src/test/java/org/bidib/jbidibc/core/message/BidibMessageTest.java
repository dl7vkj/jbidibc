package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.message.BidibMessage;
import org.bidib.jbidibc.core.message.StringSetMessage;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BidibMessageTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BidibMessageTest.class);

    @Test
    public void createMessageSysGetMagicFromByteArray() throws ProtocolException {
        byte[] message = { 0x03, 0x00, 0x00, 0x01, (byte) 0xD6 };

        BidibMessage bidibMessage = new BidibMessage(message);

        Assert.assertEquals(bidibMessage.getType(), BidibLibrary.MSG_SYS_GET_MAGIC);
    }

    @Test
    public void createMessageSysMagicResponseFromByteArray() throws ProtocolException {
        byte[] message = { 0x05, 0x00, 0x00, (byte) 0x81, (byte) 0xFE, (byte) 0xAF };

        BidibMessage bidibMessage = new BidibMessage(message);

        Assert.assertEquals(bidibMessage.getType(), (byte) BidibLibrary.MSG_SYS_MAGIC);
    }

    @Test
    public void createMessageWithCrcErrorFromByteArray() throws ProtocolException {
        byte[] message = { 0x05, 0x00, 0x01, (byte) 0x86, (byte) 0x02, (byte) 0x00, (byte) 0x46 };

        BidibMessage bidibMessage = new BidibMessage(message);

        Assert.assertEquals(ByteUtils.getInt(bidibMessage.getType()), BidibLibrary.MSG_SYS_ERROR);
    }

    @Test
    public void createMessageFeedbackMultipleResponseFromByteArray() throws ProtocolException {
        byte[] message =
            { 0x16, 0x01, 0x00, (byte) 0x86, (byte) 0xA2, 0x00, (byte) 0x80, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff };

        BidibMessage bidibMessage = new BidibMessage(message);

        Assert.assertEquals(bidibMessage.getType(), (byte) BidibLibrary.MSG_BM_MULTIPLE);
    }

    @Test
    public void createMessageLcConfigSet() throws ProtocolException {
        // 25.08.2013 17:25:14.248: send LcConfigSetMessage[num=245,type=65,data=[2, 0, 0, 255, 4, 0]] : fe 0a 02 00 f5
        // 41 02 00 00 ff 04 00 c4 fe
        byte[] message =
            { 0x0a, 0x02, 0x00, (byte) 0xf5, 0x41, 0x02, 0x00, 0x00, (byte) 0xff, 0x04, 0x00, (byte) 0xc4 };

        BidibMessage bidibMessage = new BidibMessage(message);

        Assert.assertEquals(ByteUtils.getInt(bidibMessage.getType()), BidibLibrary.MSG_LC_CONFIG_SET);
    }

    @Test
    public void createMessageBoostOn() throws ProtocolException {
        // 08.11.2013 13:43:46.017: send BoostOnMessage[num=20,type=49,data=[1]] : fe 05 00 14 31 01 00 6c fe
        byte[] message = { 0x05, 0x00, 0x14, (byte) 0x31, 0x01, 0x00, (byte) 0x6c };

        BidibMessage bidibMessage = new BidibMessage(message);

        Assert.assertEquals(ByteUtils.getInt(bidibMessage.getType()), BidibLibrary.MSG_BOOST_ON);
    }

    @Test
    public void createMessageSysClock() throws ProtocolException {
        // 17.11.2013 19:30:48.688: send SysClockMessage[num=24,type=24,data=[1, 134, 70, 193]] : FE 08 00 18 18 01 86
        // 46 C1 00 20 FE

        byte[] message = { 0x08, 0x00, 0x18, 0x18, 0x01, (byte) 0x86, (byte) 0x46, (byte) 0xC1, 0x00, 0x20 };

        BidibMessage bidibMessage = new BidibMessage(message);
        LOGGER.info("bidibMessage: {}, message: {}", bidibMessage, ByteUtils.bytesToHex(message));
        Assert.assertEquals(ByteUtils.getInt(bidibMessage.getType()), BidibLibrary.MSG_SYS_CLOCK);
    }

    @Test
    public void createMessageStringSet() throws ProtocolException {
        byte[] message =
            { 0x0B, 0x00, 0x14, (byte) BidibLibrary.MSG_STRING_SET, 0x00, 0x00, (byte) 0x05, 0x42, 0x69, 0x44, 0x69,
                0x42 };

        BidibMessage bidibMessage = new BidibMessage(message);

        Assert.assertEquals(ByteUtils.getInt(bidibMessage.getType()), BidibLibrary.MSG_STRING_SET);
        byte[] data1 = bidibMessage.getData();

        StringSetMessage stringSetMessage = new StringSetMessage(0, 0, "BiDiB");
        stringSetMessage.setSendMsgNum(0x14);
        byte[] data2 = stringSetMessage.getData();

        Assert.assertEquals(data2, data1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void createMessageStringSetExceedMaxLen() {
        new StringSetMessage(
            0,
            0,
            "BiDiB with a string value that is too long. BiDiB with a string value that is too long. BiDiB with a string value that is too long");
    }
}
