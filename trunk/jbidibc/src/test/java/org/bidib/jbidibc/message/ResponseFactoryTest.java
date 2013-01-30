package org.bidib.jbidibc.message;

import org.bidib.jbidibc.exception.ProtocolException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ResponseFactoryTest {

	@Test
	public void createValidSysMagicResponseMessage() throws ProtocolException {
		byte[] message = {0x05, 0x00, 0x00, (byte) 0x81, (byte) 0xFE, (byte) 0xAF};

		BidibMessage bidibMessage = ResponseFactory.create(message);

		Assert.assertNotNull(bidibMessage);
		Assert.assertTrue(bidibMessage instanceof SysMagicResponse, "Expected a SysMagicResponse message.");
	}

	@Test(expectedExceptions=ProtocolException.class, expectedExceptionsMessageRegExp="no magic received")
	public void createInvalidSysMagicResponseMessage() throws ProtocolException {
		byte[] message = {0x05, 0x00, 0x00, (byte) 0x81, (byte) 0xFE, (byte) 0xAD};

		ResponseFactory.create(message);

		Assert.fail("Should have thrown an exception!");
	}

	@Test
	public void createValidNodeTabCountResponseMessage() throws ProtocolException {
		byte[] message = {0x04, 0x00, 0x01, (byte) 0x88, 0x01};

		BidibMessage bidibMessage = ResponseFactory.create(message);

		Assert.assertNotNull(bidibMessage);
		Assert.assertTrue(bidibMessage instanceof NodeTabCountResponse, "Expected a NodeTabCountResponse message.");
	}

	@Test
	public void createValidNodeTabResponseMessage() throws ProtocolException {
		byte[] message = {0x0c, 0x00, 0x02, (byte) 0x89, 0x01, 0x00, (byte) 0xc0, 0x00, 0x0d, 0x68, 0x00, 0x01, 0x00};

		BidibMessage bidibMessage = ResponseFactory.create(message);

		Assert.assertNotNull(bidibMessage);
		Assert.assertTrue(bidibMessage instanceof NodeTabResponse, "Expected a NodeTabResponse message.");
	}
	
	@Test
	public void createValidFeatureResponseMessage() throws ProtocolException {
		byte[] message = {0x05, 0x00, 0x01, (byte) 0x90, 0x00, 0x10};

		BidibMessage bidibMessage = ResponseFactory.create(message);

		Assert.assertNotNull(bidibMessage);
		Assert.assertTrue(bidibMessage instanceof FeatureResponse, "Expected a FeatureResponse message.");
	}

	@Test
	public void createValidFeatureNotAvailableResponseMessage() throws ProtocolException {
		byte[] message = {0x04, 0x00, 0x04, (byte) 0x91, (byte) 0xfd, (byte) 0xde};

		BidibMessage bidibMessage = ResponseFactory.create(message);

		Assert.assertNotNull(bidibMessage);
		Assert.assertTrue(bidibMessage instanceof FeatureNotAvailableResponse, "Expected a FeatureNotAvailableResponse message.");
	}
	
	@Test(expectedExceptions=ProtocolException.class, expectedExceptionsMessageRegExp="got unknown response with type 223")
	public void createUndefinedResponseMessage() throws ProtocolException {
		byte[] message = {0x04, 0x00, 0x01, (byte) 0xDF, 0x01};

		ResponseFactory.create(message);

		Assert.fail("Should have thrown an exception!");
	}
}
