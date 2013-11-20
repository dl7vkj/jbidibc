package org.bidib.jbidibc.node;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bidib.jbidibc.BidibInterface;
import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.LcConfig;
import org.bidib.jbidibc.MessageReceiver;
import org.bidib.jbidibc.VendorData;
import org.bidib.jbidibc.enumeration.LcOutputType;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.message.BidibMessage;
import org.bidib.jbidibc.message.LcConfigSetMessage;
import org.bidib.jbidibc.message.ResponseFactory;
import org.bidib.jbidibc.message.SysMagicMessage;
import org.bidib.jbidibc.utils.ByteUtils;
import org.junit.Assert;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class BidibNodeTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BidibNodeTest.class);

    private int responseCounter = 1;

    @Test
    public void vendorGetBulk() throws ProtocolException {
        final byte[] address = new byte[] { 0 };

        BidibInterface bidib = Mockito.mock(BidibInterface.class);

        MessageReceiver messageReceiver = null;
        boolean ignoreWaitTimeout = false;
        final BidibNode bidibNode = new BidibNode(address, messageReceiver, ignoreWaitTimeout);
        bidibNode.setNodeMagic(BidibLibrary.BIDIB_SYS_MAGIC);
        bidibNode.setBidib(bidib);

        Mockito.doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                //                Object[] args = invocation.getArguments();
                //                Mock mock = invocation.getMock();
                byte[] message =
                    { 0x09, 0x00, 0x05, (byte) 0x93, 0x01, 0x33, 0x03, 0x31, 0x30, 0x32, (byte) 0xBA, (byte) 0xFE };
                BidibMessage response;
                try {
                    LOGGER.info("Create new response, responseCounter: {}", responseCounter);
                    message[5] = ByteUtils.getLowByte(responseCounter + 0x30);
                    response = ResponseFactory.create(message);
                    bidibNode.getReceiveQueue().add(response);
                    responseCounter++;
                }
                catch (ProtocolException ex) {
                    LOGGER.warn("Put message to receive queue failed.", ex);
                }
                return null;
            }
        }).when(bidib).send(Mockito.any(byte[].class));

        String cvNumbers = "1,2,3,4,5,6";
        List<VendorData> vendorDatas =
            bidibNode.vendorGetBulk(Arrays.asList(StringUtils.splitPreserveAllTokens(cvNumbers, ",")));

        Assert.assertNotNull(vendorDatas);
        Assert.assertEquals(6, vendorDatas.size());
    }

    @Test
    public void encodeSysMagicMessage() throws IOException {
        final byte[] address = new byte[] { 1, 0 };

        BidibInterface bidib = Mockito.mock(BidibInterface.class);

        MessageReceiver messageReceiver = null;
        boolean ignoreWaitTimeout = false;
        final BidibNode bidibNode = new BidibNode(address, messageReceiver, ignoreWaitTimeout);
        bidibNode.setNodeMagic(BidibLibrary.BIDIB_SYS_MAGIC);
        bidibNode.setBidib(bidib);

        BidibMessage bidibMessage = new SysMagicMessage();

        byte[] message = bidibNode.encodeMessageWithStream(bidibMessage);

        Assert.assertNotNull(message);
        LOGGER.info("Encoded message: {}", ByteUtils.bytesToHex(message));

        Assert.assertEquals(7, message.length);
    }

    @Test
    public void encodeLcConfigSetMessageWithStream() throws IOException {
        final byte[] address = new byte[] { 1, 0 };

        BidibInterface bidib = Mockito.mock(BidibInterface.class);

        MessageReceiver messageReceiver = null;
        boolean ignoreWaitTimeout = false;
        final BidibNode bidibNode = new BidibNode(address, messageReceiver, ignoreWaitTimeout);
        bidibNode.setNodeMagic(BidibLibrary.BIDIB_SYS_MAGIC);
        bidibNode.setBidib(bidib);

        int port = 2;
        int pwmMin = 2;
        int pwmMax = 50;
        int dimMin = 10;
        int dimMax = 20;
        LcConfig config = new LcConfig(LcOutputType.LIGHTPORT, port, pwmMin, pwmMax, dimMax, dimMin);
        BidibMessage bidibMessage = new LcConfigSetMessage(config);

        long start = System.nanoTime();
        byte[] message = bidibNode.encodeMessageWithStream(bidibMessage);
        long end = System.nanoTime();

        LOGGER.info("Encoding duration with stream: {}", end - start);

        Assert.assertNotNull(message);
        LOGGER.info("Encoded message: {}", ByteUtils.bytesToHex(message));

        Assert.assertEquals(13, message.length);
    }

    @Test
    public void encodeLcConfigSetMessage() {
        final byte[] address = new byte[] { 1, 0 };

        BidibInterface bidib = Mockito.mock(BidibInterface.class);

        MessageReceiver messageReceiver = null;
        boolean ignoreWaitTimeout = false;
        final BidibNode bidibNode = new BidibNode(address, messageReceiver, ignoreWaitTimeout);
        bidibNode.setNodeMagic(BidibLibrary.BIDIB_SYS_MAGIC);
        bidibNode.setBidib(bidib);

        int port = 2;
        int pwmMin = 2;
        int pwmMax = 50;
        int dimMin = 10;
        int dimMax = 20;
        LcConfig config = new LcConfig(LcOutputType.LIGHTPORT, port, pwmMin, pwmMax, dimMax, dimMin);
        BidibMessage bidibMessage = new LcConfigSetMessage(config);

        long start = System.nanoTime();
        byte[] message = bidibNode.encodeMessage(bidibMessage);
        long end = System.nanoTime();

        LOGGER.info("Encoding duration: {}", end - start);

        Assert.assertNotNull(message);
        LOGGER.info("Encoded message: {}", ByteUtils.bytesToHex(message));

        Assert.assertEquals(12, message.length);
    }
}
