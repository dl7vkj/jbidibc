package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.Node;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.NodeUtils;
import org.bidib.jbidibc.utils.NodeUtilsTest;
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
        Assert.assertEquals(bidibMessage.getType(), (byte) BidibLibrary.MSG_SYS_MAGIC);
        Assert.assertEquals(((SysMagicResponse) bidibMessage).getMagic(), 0xAFFE);
    }

    @Test
    public void createMessageBootMagicResponseFromByteArray() throws ProtocolException {
        byte[] message = { 0x05, 0x00, 0x00, (byte) 0x81, (byte) 0x0D, (byte) 0xB0 };

        BidibMessage bidibMessage = ResponseFactory.create(message);

        Assert.assertNotNull(bidibMessage);
        Assert.assertTrue(bidibMessage instanceof SysMagicResponse, "Expected a SysMagicResponse message.");
        Assert.assertEquals(bidibMessage.getType(), (byte) BidibLibrary.MSG_SYS_MAGIC);
        Assert.assertEquals(((SysMagicResponse) bidibMessage).getMagic(), 0xB00D);
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

        NodeTabResponse nodeTabResponse = (NodeTabResponse) bidibMessage;
        Node node = nodeTabResponse.getNode(nodeTabResponse.getAddr());
        Assert.assertNotNull(node);
        Assert.assertEquals(node.getAddr(), new byte[] { 0 });
    }

    @Test
    public void createValidNodeTabResponse2Message() throws ProtocolException {
        // 18.08.2013 09:07:38.188: receive NodeTabResponse[[0],num=1,type=137,data=[1, 1, 5, 0, 13, 108, 0, 51, 0]] : 0c 00 01 89 01 01 05 00 0d 6c 00 33 00 

        byte[] message =
            new byte[] { 0x0c, 0x00, 0x01, (byte) 0x89, 0x01, 0x01, 0x05, 0x00, (byte) 0x0d, (byte) 0x6c, 0x00, 0x33,
                0x00 };
        BidibMessage bidibMessage = ResponseFactory.create(message);

        Assert.assertNotNull(bidibMessage);
        Assert.assertTrue(bidibMessage instanceof NodeTabResponse, "Expected a NodeTabResponse message.");

        // 09:07:38.148 [INFO] org.bidib.jbidibc.message.NodeTabResponse [main] - Created new node: Node[version=1,addr=[0],uniqueId=0xd2000d68000036]
        Node parentNode = new Node(1, new byte[] { 0 }, NodeUtilsTest.prepareUniqueid("d2000d68000036"));

        NodeTabResponse nodeTabResponse = (NodeTabResponse) bidibMessage;
        Node node = nodeTabResponse.getNode(parentNode.getAddr());
        Assert.assertNotNull(node);
        Assert.assertEquals(node.getAddr(), new byte[] { 1 });
    }

    @Test
    public void createValidNodeTabResponse4Message() throws ProtocolException {
        // 15:03:15.910 [DEBUG] org.bidib.jbidibc.MessageReceiver [Thread-9] - Received raw message: 0d 01 00 03 89 02 00 81 00 0d 72 00 1e 00 16 fe 

        // message: length msg_addr msg_num msg_type data
        // 
        // 0x0d : len -> 13
        // 01 00 : msg_addr -> 1
        // 03 : msg_num -> 3
        // 0x89 : msg_type -> node tab response

        // nodetab_data: nodetab_version nodetab_entry
        // 02 : version -> 2
        // nodetab_entry: node_addr uniqueId
        // 00 : node_addr -> 0
        // 81 00 0d 72 00 1e 00 16 fe

        byte[] message =
            new byte[] { 0x0d, 0x01, 0x00, 0x03, (byte) 0x89, 0x02, 0x00, (byte) 0x81, 0x00, (byte) 0x0d, (byte) 0x72,
                0x00, 0x1e, 0x00, 0x16, (byte) 0xfe };
        BidibMessage bidibMessage = ResponseFactory.create(message);

        Assert.assertNotNull(bidibMessage);
        Assert.assertTrue(bidibMessage instanceof NodeTabResponse, "Expected a NodeTabResponse message.");

        // 09:07:38.148 [INFO] org.bidib.jbidibc.message.NodeTabResponse [main] - Created new node: Node[version=1,addr=[0],uniqueId=0xd2000d68000036]
        Node parentNode = new Node(1, new byte[] { 1 }, NodeUtilsTest.prepareUniqueid("81000d72001e00"));

        NodeTabResponse nodeTabResponse = (NodeTabResponse) bidibMessage;
        Node node = nodeTabResponse.getNode(parentNode.getAddr());
        Assert.assertNotNull(node);
        Assert.assertEquals(node.getAddr(), new byte[] { 1 });
    }

    @Test
    public void createValidNodeNewResponseMessage() throws ProtocolException {
        // 09:34:34.047 [DEBUG] org.bidib.jbidibc.MessageReceiver [Thread-9] - Received raw message: 0c 00 21 8d 06 01 05 00 0d 6b 00 69 ea aa fe 

        byte[] message =
            new byte[] { 0x0c, 0x00, 0x21, (byte) 0x8d, 0x06, 0x01, 0x05, 0x00, (byte) 0x0d, (byte) 0x6b, 0x00, 0x69,
                (byte) 0xea, (byte) 0xaa, (byte) 0xfe };
        BidibMessage bidibMessage = ResponseFactory.create(message);

        Assert.assertNotNull(bidibMessage);
        Assert.assertTrue(bidibMessage instanceof NodeNewResponse, "Expected a NodeNewResponse message.");

        // 09:07:38.148 [INFO] org.bidib.jbidibc.message.NodeTabResponse [main] - Created new node: Node[version=1,addr=[0],uniqueId=0xd2000d68000036]
        //        Node parentNode = new Node(1, new byte[] { 0 }, NodeUtilsTest.prepareUniqueid("d2000d68000036"));

        NodeNewResponse nodeNewResponse = (NodeNewResponse) bidibMessage;
        Node node = nodeNewResponse.getNode(bidibMessage.getAddr());
        Assert.assertNotNull(node);
        Assert.assertEquals(node.getAddr(), new byte[] { 1 });
    }

    @Test
    public void createValidNodeNewResponse2Message() throws ProtocolException {
        // 09:34:34.047 [DEBUG] org.bidib.jbidibc.MessageReceiver [Thread-9] - Received raw message: 0c 00 21 8d 06 01 05 00 0d 6b 00 69 ea aa fe 
        // 21:55:23.149 [DEBUG] org.bidib.jbidibc.MessageReceiver [Thread-9] - Received raw message: 0d 01 00 01 8d 06 01 42 00 0d 67 00 65 ea fb fe 
        // 15:03:15.910 [DEBUG] org.bidib.jbidibc.MessageReceiver [Thread-9] - Received raw message: 0d 01 00 03 89 02 00 81 00 0d 72 00 1e 00 16 fe 

        // message: length msg_addr msg_num msg_type data
        // 
        // 0x0d : len -> 13
        // 01 00 : msg_addr -> 1
        // 01 : msg_num -> 1
        // 0x8d : msg_type -> node new

        // nodetab_data: nodetab_version nodetab_entry
        // 06 : version -> 6
        // nodetab_entry: node_addr uniqueId
        // 01 : node_addr -> 1
        // 42 00 0d 67 00 65 ea fb fe

        byte[] message =
            new byte[] { 0x0D, 0x01, 0x00, 0x01, (byte) 0x8d, 0x06, 0x01, 0x42, 0x00, (byte) 0x0d, (byte) 0x67, 0x00,
                0x65, (byte) 0xea, (byte) 0xfb, (byte) 0xfe };
        BidibMessage bidibMessage = ResponseFactory.create(message);

        Assert.assertNotNull(bidibMessage);
        Assert.assertTrue(bidibMessage instanceof NodeNewResponse, "Expected a NodeNewResponse message.");

        // 09:07:38.148 [INFO] org.bidib.jbidibc.message.NodeTabResponse [main] - Created new node: Node[version=1,addr=[0],uniqueId=0xd2000d68000036]
        //        Node parentNode = new Node(1, new byte[] { 0 }, NodeUtilsTest.prepareUniqueid("d2000d68000036"));

        NodeNewResponse nodeNewResponse = (NodeNewResponse) bidibMessage;
        Node node = nodeNewResponse.getNode(bidibMessage.getAddr());
        Assert.assertNotNull(node);
        Assert.assertEquals(node.getAddr(), new byte[] { 1, 1 });
    }

    //    [20130817165457078] Input  : |0C 00 7C 8D 02 01 81 00 0D 72 00 1F 00 45 FE |
    //    [20130817165457109] IN --- : MSG_NODE_NEW              0C 00 7C 8D 02 01 81 00 0D 72 00 1F 00 
    //    Node Count 2
    //    Node added : 81.00.0D.72.00.1F.00 with Adr : 1.0.0.0
    // 
    //    [20130817165457531] Input  : |0D 01 00 06 89 01 00 81 00 0D 72 00 1F 00 8D FE |
    //
    //    [20130817165509375] Input  : |0D 01 00 14 8D 02 01 42 00 0D 67 00 65 EA 16 FE |
    //    [20130817165509406] IN --- : MSG_NODE_NEW              0D 01 00 14 8D 02 01 42 00 0D 67 00 65 EA 
    //    Node Count 3
    //    Node added : 42.00.0D.67.00.65.EA with Adr : 1.1.0.0

    @Test
    public void createValidNodeTabResponse3Message() throws ProtocolException {
        // 14.08.2013 21:55:23.149: receive NodeNewResponse[[1, 0],num=1,type=141,data=[6, 1, 66, 0, 13, 103, 0, 101, 234]] : 0d 01 00 01 8d 06 01 42 00 0d 67 00 65 ea 

        byte[] message =
            new byte[] { 0x0d, 0x01, 0x00, 0x01, (byte) 0x8d, 0x06, 0x01, (byte) 0x42, 0x00, (byte) 0x0d, (byte) 0x67,
                0x00, 0x65, (byte) 0xea };

        BidibMessage bidibMessage = ResponseFactory.create(message);

        Assert.assertNotNull(bidibMessage);
        Assert.assertTrue(bidibMessage instanceof NodeTabResponse, "Expected a NodeTabResponse message.");

        // Create new node has finished: Node[version=2,addr=[1, 0],uniqueId=0x81000d72001f00]
        Node parentNode = new Node(2, new byte[] { 1 }, NodeUtilsTest.prepareUniqueid("81000d72001f00"));

        NodeTabResponse nodeTabResponse = (NodeTabResponse) bidibMessage;
        Node node = nodeTabResponse.getNode(parentNode.getAddr());
        Assert.assertNotNull(node);

        Assert.assertEquals(node.getAddr(), new byte[] { 1, 1 });
    }

    // 15:03:15.910 [DEBUG] org.bidib.jbidibc.MessageReceiver [Thread-9] - Received raw message: 0d 01 00 03 89 02 00 81 00 0d 72 00 1e 00 16 fe 

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
        byte[] message =
            { 0x16, 0x01, 0x00, (byte) 0x86, (byte) 0xA2, 0x00, (byte) 0x80, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff };

        BidibMessage bidibMessage = ResponseFactory.create(message);
        Assert.assertEquals(bidibMessage.getType(), (byte) BidibLibrary.MSG_BM_MULTIPLE);

        Assert.assertEquals(((FeedbackMultipleResponse) bidibMessage).getSize(), 0x80);
    }

    @Test
    public void createMessageCommandStationDriveManualResponseFromByteArray() throws ProtocolException {
        byte[] message = { 12, 0, 20, -27, 94, 0, 2, 2, 0, 16, 0, 0, 0 };

        BidibMessage bidibMessage = ResponseFactory.create(message);
        Assert.assertEquals(bidibMessage.getType(), (byte) BidibLibrary.MSG_CS_DRIVE_MANUAL);

        Assert.assertEquals(((CommandStationDriveManualResponse) bidibMessage).getAddress(), 94);
        Assert.assertEquals(((CommandStationDriveManualResponse) bidibMessage).getSpeed(), 0);
    }

}
