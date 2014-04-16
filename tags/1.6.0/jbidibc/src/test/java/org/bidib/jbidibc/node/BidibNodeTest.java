package org.bidib.jbidibc.node;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bidib.jbidibc.BidibInterface;
import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.LcConfig;
import org.bidib.jbidibc.MessageReceiver;
import org.bidib.jbidibc.StringData;
import org.bidib.jbidibc.VendorData;
import org.bidib.jbidibc.enumeration.LcOutputType;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.message.BidibCommand;
import org.bidib.jbidibc.message.BidibMessage;
import org.bidib.jbidibc.message.FeedbackMirrorFreeMessage;
import org.bidib.jbidibc.message.LcConfigSetMessage;
import org.bidib.jbidibc.message.RequestFactory;
import org.bidib.jbidibc.message.ResponseFactory;
import org.bidib.jbidibc.node.BidibNode.EncodedMessage;
import org.bidib.jbidibc.utils.ByteUtils;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class BidibNodeTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BidibNodeTest.class);

    private int responseCounter = 1;

    private RequestFactory requestFactory;

    @BeforeTest
    public void prepare() {
        LOGGER.info("Prepare the request factory.");
        requestFactory = new RequestFactory();
    }

    @Test
    public void setStringTest() throws ProtocolException {
        final byte[] address = new byte[] { 0 };

        BidibInterface bidib = Mockito.mock(BidibInterface.class);

        MessageReceiver messageReceiver = null;
        boolean ignoreWaitTimeout = false;
        final BidibNode bidibNode = new BidibNode(address, messageReceiver, ignoreWaitTimeout);
        bidibNode.setNodeMagic(BidibLibrary.BIDIB_SYS_MAGIC);
        bidibNode.setBidib(bidib);
        bidibNode.setRequestFactory(requestFactory);

        Mockito.doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                byte[] bytes = (byte[]) args[0];
                LOGGER.info("Received data: {}", ByteUtils.bytesToHex(bytes));
                byte namespace = bytes[5];
                byte index = bytes[6];

                byte[] message =
                    { 0x15, 0x00, 0x14, (byte) BidibLibrary.MSG_STRING, namespace, index, (byte) 0x0F, 0x42, 0x69,
                        0x44, 0x69, 0x42, 0x20, 0x54, 0x65, 0x73, 0x74, 0x76, 0x61, 0x6C, 0x75, 0x65 };
                BidibMessage response;
                try {
                    LOGGER.info("Create new response, responseCounter: {}", responseCounter);
                    response = ResponseFactory.create(message);
                    bidibNode.getReceiveQueue().add(response);
                }
                catch (ProtocolException ex) {
                    LOGGER.warn("Put message to receive queue failed.", ex);
                }

                return null;
            }
        }).when(bidib).send(Mockito.any(byte[].class));

        int namespace = 0;
        int index = 1;
        String value = "BiDiB Testvalue";
        StringData result = bidibNode.setString(namespace, index, value);
        LOGGER.info("Returned StringData result: {}", result);
        Mockito.verify(bidib, Mockito.times(1)).send(Mockito.any(byte[].class));

        Assert.assertEquals(namespace, result.getNamespace());
        Assert.assertEquals(index, result.getIndex());
        Assert.assertEquals(value, result.getValue());
    }

    // 25.01.2014 10:42:41.032: send StringSetMessage[num=88,type=26,data=[0, 1, 16, 71, 97, 110, 122, 32, 108, 97, 110,
    // 103, 101, 114, 32, 78, 97, 109, 101]]
    // : FE 17 01 00 58 1A 00 01 10 47 61 6E 7A 20 6C 61 6E 67 65 72 20 4E 61 6D 65 0B FE
    // 25.01.2014 10:42:41.188: receive StringResponse[[1],num=89,type=149,data=[0, 1, 16, 71, 97, 110, 122, 32, 108,
    // 97, 110, 103, 101, 114, 32, 78, 97, 109, 101]]
    // : 17 01 00 59 95 00 01 10 47 61 6E 7A 20 6C 61 6E 67 65 72 20 4E 61 6D 65

    @Test
    public void setStringTest2() throws ProtocolException {
        final byte[] address = new byte[] { 0x01 };

        BidibInterface bidib = Mockito.mock(BidibInterface.class);

        MessageReceiver messageReceiver = null;
        boolean ignoreWaitTimeout = false;
        final BidibNode bidibNode = new BidibNode(address, messageReceiver, ignoreWaitTimeout);
        bidibNode.setNodeMagic(BidibLibrary.BIDIB_SYS_MAGIC);
        bidibNode.setBidib(bidib);
        bidibNode.setRequestFactory(requestFactory);

        Mockito.doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                byte[] bytes = (byte[]) args[0];
                LOGGER.info("Received data: {}", ByteUtils.bytesToHex(bytes));
                byte namespace = bytes[6];
                byte index = bytes[7];

                byte[] message =
                    { 0x17, 0x01, 0x00, 0x59, (byte) BidibLibrary.MSG_STRING, namespace, index, (byte) 0x10, 0x47,
                        0x61, 0x6E, 0x7A, 0x20, 0x6C, 0x61, 0x6E, 0x67, 0x65, 0x72, 0x20, 0x4E, 0x61, 0x6D, 0x65 };
                BidibMessage response;
                try {
                    LOGGER.info("Create new response, responseCounter: {}", responseCounter);
                    response = ResponseFactory.create(message);
                    bidibNode.getReceiveQueue().add(response);
                }
                catch (ProtocolException ex) {
                    LOGGER.warn("Put message to receive queue failed.", ex);
                }

                return null;
            }
        }).when(bidib).send(Mockito.any(byte[].class));

        int namespace = 0;
        int index = 1;
        String value = "Ganz langer Name";
        StringData result = bidibNode.setString(namespace, index, value);
        LOGGER.info("Returned StringData result: {}", result);
        Mockito.verify(bidib, Mockito.times(1)).send(Mockito.any(byte[].class));

        Assert.assertEquals(namespace, result.getNamespace());
        Assert.assertEquals(index, result.getIndex());
        Assert.assertEquals(value, result.getValue());
    }

    @Test
    public void getStringTest() throws ProtocolException {
        final byte[] address = new byte[] { 0 };

        BidibInterface bidib = Mockito.mock(BidibInterface.class);

        MessageReceiver messageReceiver = null;
        boolean ignoreWaitTimeout = false;
        final BidibNode bidibNode = new BidibNode(address, messageReceiver, ignoreWaitTimeout);
        bidibNode.setNodeMagic(BidibLibrary.BIDIB_SYS_MAGIC);
        bidibNode.setBidib(bidib);
        bidibNode.setRequestFactory(requestFactory);

        Mockito.doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                byte[] bytes = (byte[]) args[0];
                LOGGER.info("Received data: {}", ByteUtils.bytesToHex(bytes));
                byte namespace = bytes[5];
                byte index = bytes[6];

                byte[] message =
                    { 0x15, 0x00, 0x14, (byte) BidibLibrary.MSG_STRING, namespace, index, (byte) 0x0F, 0x42, 0x69,
                        0x44, 0x69, 0x42, 0x20, 0x54, 0x65, 0x73, 0x74, 0x76, 0x61, 0x6C, 0x75, 0x65 };
                BidibMessage response;
                try {
                    LOGGER.info("Create new response, responseCounter: {}", responseCounter);
                    response = ResponseFactory.create(message);
                    bidibNode.getReceiveQueue().add(response);
                }
                catch (ProtocolException ex) {
                    LOGGER.warn("Put message to receive queue failed.", ex);
                }

                return null;
            }
        }).when(bidib).send(Mockito.any(byte[].class));

        int namespace = 1;
        int index = 1;
        String value = "BiDiB Testvalue";
        StringData result = bidibNode.getString(namespace, index);
        LOGGER.info("Returned StringData result: {}", result);
        Mockito.verify(bidib, Mockito.times(1)).send(Mockito.any(byte[].class));

        Assert.assertEquals(namespace, result.getNamespace());
        Assert.assertEquals(index, result.getIndex());
        Assert.assertEquals(value, result.getValue());
    }

    @Test
    public void vendorGetBulk() throws ProtocolException {
        final byte[] address = new byte[] { 0 };

        BidibInterface bidib = Mockito.mock(BidibInterface.class);

        MessageReceiver messageReceiver = null;
        boolean ignoreWaitTimeout = false;
        final BidibNode bidibNode = new BidibNode(address, messageReceiver, ignoreWaitTimeout);
        bidibNode.setNodeMagic(BidibLibrary.BIDIB_SYS_MAGIC);
        bidibNode.setBidib(bidib);
        bidibNode.setRequestFactory(requestFactory);

        Mockito.doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                LOGGER.info("The args: {}", args);

                byte[] bytes = (byte[]) args[0];
                LOGGER.info("Received data: {}", ByteUtils.bytesToHex(bytes));
                // detect how many messages were received

                int count = 0;
                int index = 1;
                while (index < (bytes.length - 2)) {
                    int lenOfMessage = (bytes[index] & 0xFF);

                    index += lenOfMessage + 1;

                    count++;
                    LOGGER.info("len: {}, index: {}, count: {}", lenOfMessage, index, count);
                }

                for (int current = 0; current < count; current++) {
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
                }
                return null;
            }
        }).when(bidib).send(Mockito.any(byte[].class));

        String cvNumbers = "1,2,3,4,5,6,7,8,9";
        List<VendorData> vendorDatas =
            bidibNode.vendorGetBulk(Arrays.asList(StringUtils.splitPreserveAllTokens(cvNumbers, ",")));

        Assert.assertNotNull(vendorDatas);
        Assert.assertEquals(9, vendorDatas.size());

        // send is called 3 times (4 combined + 4 combined + 1)
        Mockito.verify(bidib, Mockito.times(3)).send(Mockito.any(byte[].class));
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
        bidibNode.setRequestFactory(requestFactory);

        BidibCommand bidibMessage = requestFactory.createSysMagic();

        BidibNode.EncodedMessage message = bidibNode.encodeMessage(bidibMessage);

        Assert.assertNotNull(message);
        LOGGER.info("Encoded message: {}", ByteUtils.bytesToHex(message.getMessage()));

        Assert.assertEquals(6, message.getMessage().length);
    }

    @Test
    public void encodeLightPortLcConfigSetMessage() {
        final byte[] address = new byte[] { 1, 0 };

        BidibInterface bidib = Mockito.mock(BidibInterface.class);

        MessageReceiver messageReceiver = null;
        boolean ignoreWaitTimeout = false;
        final BidibNode bidibNode = new BidibNode(address, messageReceiver, ignoreWaitTimeout);
        bidibNode.setNodeMagic(BidibLibrary.BIDIB_SYS_MAGIC);
        bidibNode.setBidib(bidib);
        bidibNode.setRequestFactory(requestFactory);

        int port = 2;
        int pwmMin = 2;
        int pwmMax = 50;
        int dimMin = 10;
        int dimMax = 20;
        LcConfig config = new LcConfig(LcOutputType.LIGHTPORT, port, pwmMin, pwmMax, dimMax, dimMin);
        BidibCommand bidibMessage = new LcConfigSetMessage(config);

        long start = System.nanoTime();
        EncodedMessage message = bidibNode.encodeMessage(bidibMessage);
        long end = System.nanoTime();

        LOGGER.info("Encoding duration: {}", end - start);

        Assert.assertNotNull(message);
        LOGGER.info("Encoded message: {}", ByteUtils.bytesToHex(message.getMessage()));

        Assert.assertEquals(12, message.getMessage().length);
    }

    @Test
    public void encodeSwitchPortLcConfigSetMessage() {
        final byte[] address = new byte[] { 1, 0 };

        BidibInterface bidib = Mockito.mock(BidibInterface.class);

        MessageReceiver messageReceiver = null;
        boolean ignoreWaitTimeout = false;
        final BidibNode bidibNode = new BidibNode(address, messageReceiver, ignoreWaitTimeout);
        bidibNode.setNodeMagic(BidibLibrary.BIDIB_SYS_MAGIC);
        bidibNode.setBidib(bidib);
        bidibNode.setRequestFactory(requestFactory);

        int port = 2;
        int ioBehaviour = 1;
        int switchOffTime = 20;
        LcConfig config = new LcConfig(LcOutputType.SWITCHPORT, port, ioBehaviour, switchOffTime, 0, 0);
        BidibCommand bidibMessage = new LcConfigSetMessage(config);

        long start = System.nanoTime();
        EncodedMessage message = bidibNode.encodeMessage(bidibMessage);
        long end = System.nanoTime();

        LOGGER.info("Encoding duration: {}", end - start);

        Assert.assertNotNull(message);
        LOGGER.info("Encoded message: {}", ByteUtils.bytesToHex(message.getMessage()));

        Assert.assertEquals(12, message.getMessage().length);
        Assert.assertEquals(2, message.getMessage()[7]);
        Assert.assertEquals(1, message.getMessage()[8]);
        Assert.assertEquals(20, message.getMessage()[9]);
    }

    @Test
    public void encodeBmMirorFreeMessage() throws IOException {
        final byte[] address = new byte[] { 5 };

        BidibInterface bidib = Mockito.mock(BidibInterface.class);

        MessageReceiver messageReceiver = null;
        boolean ignoreWaitTimeout = false;
        final BidibNode bidibNode = new BidibNode(address, messageReceiver, ignoreWaitTimeout);
        bidibNode.setNodeMagic(BidibLibrary.BIDIB_SYS_MAGIC);
        bidibNode.setBidib(bidib);
        bidibNode.setRequestFactory(requestFactory);

        int detectorNumber = 8;
        BidibCommand bidibMessage = new FeedbackMirrorFreeMessage(detectorNumber);

        long start = System.nanoTime();
        EncodedMessage message = bidibNode.encodeMessage(bidibMessage);
        long end = System.nanoTime();

        LOGGER.info("Encoding duration with stream: {}", end - start);

        Assert.assertNotNull(message);
        LOGGER.info("Encoded message: {}", ByteUtils.bytesToHex(message.getMessage()));

        Assert.assertEquals(6, message.getMessage().length);
    }

    @Test
    public void bmMirorFree() throws ProtocolException {
        final byte[] address = new byte[] { 5 };

        BidibInterface bidib = Mockito.mock(BidibInterface.class);

        MessageReceiver messageReceiver = null;
        boolean ignoreWaitTimeout = false;
        final BidibNode bidibNode = new BidibNode(address, messageReceiver, ignoreWaitTimeout);
        bidibNode.setNodeMagic(BidibLibrary.BIDIB_SYS_MAGIC);
        bidibNode.setBidib(bidib);
        bidibNode.setRequestFactory(requestFactory);

        Mockito.doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                // Mock mock = invocation.getMock();

                LOGGER.debug("Called with: {}", ByteUtils.bytesToHex((byte[]) args[0]));
                byte[] message = (byte[]) args[0];
                Assert.assertEquals(new byte[] { (byte) 0xFE, 0x05, 0x05, 0x00, 0x00, 0x23, 0x08, 0x6F, (byte) 0xFE },
                    message);
                return null;
            }
        }).when(bidib).send(Mockito.any(byte[].class));

        int detectorNumber = 8;
        bidibNode.acknowledgeFree(detectorNumber);

        Mockito.verify(bidib, Mockito.times(1)).send(Mockito.any(byte[].class));
    }

    @Test
    public void bmMirorFreeOnRootNode() throws ProtocolException {
        final byte[] address = new byte[] { 0 };

        BidibInterface bidib = Mockito.mock(BidibInterface.class);

        MessageReceiver messageReceiver = null;
        boolean ignoreWaitTimeout = false;
        final BidibNode bidibNode = new BidibNode(address, messageReceiver, ignoreWaitTimeout);
        bidibNode.setNodeMagic(BidibLibrary.BIDIB_SYS_MAGIC);
        bidibNode.setBidib(bidib);
        bidibNode.setRequestFactory(requestFactory);

        Mockito.doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                // Mock mock = invocation.getMock();

                LOGGER.debug("Called with: {}", ByteUtils.bytesToHex((byte[]) args[0]));
                byte[] message = (byte[]) args[0];
                Assert.assertEquals(new byte[] { (byte) 0xFE, 0x04, 0x00, 0x00, 0x23, 0x08, 0x49, (byte) 0xFE },
                    message);
                return null;
            }
        }).when(bidib).send(Mockito.any(byte[].class));

        int detectorNumber = 8;
        bidibNode.acknowledgeFree(detectorNumber);

        Mockito.verify(bidib, Mockito.times(1)).send(Mockito.any(byte[].class));
    }
}
