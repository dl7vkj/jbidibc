package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BidibMessageTest {

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

    @Test(enabled = false)
    public void createMessageWithCrcErrorFromByteArray() throws ProtocolException {
        byte[] message = { 0x05, 0x00, 0x01, (byte) 0x86, (byte) 0x02, (byte) 0x00, (byte) 0x46 };

        BidibMessage bidibMessage = new BidibMessage(message);

        Assert.assertEquals((byte) bidibMessage.getType(), (byte) BidibLibrary.MSG_SYS_MAGIC);
    }

    @Test(enabled = false)
    public void createMessageWithError2FromByteArray() throws ProtocolException {
        byte[] message = { 0x03, 0x00, 0x00, (byte) 0x01, (byte) 0xde };

        BidibMessage bidibMessage = new BidibMessage(message);

        Assert.assertEquals((byte) bidibMessage.getType(), (byte) BidibLibrary.MSG_SYS_MAGIC);
    }
    
    // 29.05.2013 06:10:55.384: receive FeedbackMultipleResponse[[1, 0],num=134,type=162,data=[0, 128, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255]] : 16 01 00 86 a2 00 80 ff ff ff ff ff ff ff ff ff ff ff ff ff ff ff ff 

    @Test
    public void createMessageFeedbackMultipleResponseFromByteArray() throws ProtocolException {
        byte[] message = { 0x16, 0x01, 0x00, (byte) 0x86, (byte) 0xA2, 0x00, (byte) 0x80, (byte) 0xff, (byte) 0xff, 
            (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 
            (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff };

        BidibMessage bidibMessage = new BidibMessage(message);

        Assert.assertEquals(bidibMessage.getType(), (byte) BidibLibrary.MSG_BM_MULTIPLE);
    }

}
