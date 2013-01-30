package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BidibMessageTest {
	
	@Test
	public void createMessageSysGetMagicFromByteArray() throws ProtocolException {
		byte[] message = {0x03, 0x00, 0x00, 0x01, (byte) 0xD6};
		
		BidibMessage bidibMessage = new BidibMessage(message);
		
		Assert.assertEquals(bidibMessage.getType(), BidibLibrary.MSG_SYS_GET_MAGIC);
	}

	@Test
	public void createMessageSysMagicFromByteArray() throws ProtocolException {
		byte[] message = {0x05, 0x00, 0x00, (byte) 0x81, (byte) 0xFD, (byte) 0xDE, (byte) 0xAF, (byte) 0x89};
		
		BidibMessage bidibMessage = new BidibMessage(message);
		
		Assert.assertEquals(bidibMessage.getType(), (byte) BidibLibrary.MSG_SYS_MAGIC);
	}

	@Test(enabled=false)
	public void createMessageWithCrcErrorFromByteArray() throws ProtocolException {
		byte[] message = {0x05, 0x00, 0x01, (byte) 0x86, (byte) 0x02, (byte) 0x00, (byte) 0x46};
		
		BidibMessage bidibMessage = new BidibMessage(message);
		
		Assert.assertEquals((byte) bidibMessage.getType(), (byte) BidibLibrary.MSG_SYS_MAGIC);
	}


	@Test(enabled=false)
	public void createMessageWithError2FromByteArray() throws ProtocolException {
		byte[] message = {0x03, 0x00, 0x00, (byte) 0x01, (byte) 0xde};
		
		BidibMessage bidibMessage = new BidibMessage(message);
		
		Assert.assertEquals((byte) bidibMessage.getType(), (byte) BidibLibrary.MSG_SYS_MAGIC);
	}
}
