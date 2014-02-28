package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.Node;
import org.bidib.jbidibc.StringData;
import org.bidib.jbidibc.enumeration.BoosterState;
import org.bidib.jbidibc.enumeration.LcMacroState;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
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
        // 18.08.2013 09:07:38.188: receive NodeTabResponse[[0],num=1,type=137,data=[1, 1, 5, 0, 13, 108, 0, 51, 0]] :
        // 0c 00 01 89 01 01 05 00 0d 6c 00 33 00

        byte[] message =
            new byte[] { 0x0c, 0x00, 0x01, (byte) 0x89, 0x01, 0x01, 0x05, 0x00, (byte) 0x0d, (byte) 0x6c, 0x00, 0x33,
                0x00 };
        BidibMessage bidibMessage = ResponseFactory.create(message);

        Assert.assertNotNull(bidibMessage);
        Assert.assertTrue(bidibMessage instanceof NodeTabResponse, "Expected a NodeTabResponse message.");

        // 09:07:38.148 [INFO] org.bidib.jbidibc.message.NodeTabResponse [main] - Created new node:
        // Node[version=1,addr=[0],uniqueId=0xd2000d68000036]
        Node parentNode = new Node(1, new byte[] { 0 }, NodeUtilsTest.prepareUniqueid("d2000d68000036"));

        NodeTabResponse nodeTabResponse = (NodeTabResponse) bidibMessage;
        Node node = nodeTabResponse.getNode(parentNode.getAddr());
        Assert.assertNotNull(node);
        Assert.assertEquals(node.getAddr(), new byte[] { 1 });
    }

    @Test
    public void createValidNodeTabResponse4Message() throws ProtocolException {
        // 15:03:15.910 [DEBUG] org.bidib.jbidibc.MessageReceiver [Thread-9] - Received raw message: 0d 01 00 03 89 02
        // 00 81 00 0d 72 00 1e 00 16 fe

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

        // 09:07:38.148 [INFO] org.bidib.jbidibc.message.NodeTabResponse [main] - Created new node:
        // Node[version=1,addr=[0],uniqueId=0xd2000d68000036]
        Node parentNode = new Node(1, new byte[] { 1 }, NodeUtilsTest.prepareUniqueid("81000d72001e00"));

        NodeTabResponse nodeTabResponse = (NodeTabResponse) bidibMessage;
        Node node = nodeTabResponse.getNode(parentNode.getAddr());
        Assert.assertNotNull(node);
        Assert.assertEquals(node.getAddr(), new byte[] { 1 });
    }

    @Test
    public void createValidNodeNewResponseMessage() throws ProtocolException {
        // 09:34:34.047 [DEBUG] org.bidib.jbidibc.MessageReceiver [Thread-9] - Received raw message: 0c 00 21 8d 06 01
        // 05 00 0d 6b 00 69 ea aa fe

        byte[] message =
            new byte[] { 0x0c, 0x00, 0x21, (byte) 0x8d, 0x06, 0x01, 0x05, 0x00, (byte) 0x0d, (byte) 0x6b, 0x00, 0x69,
                (byte) 0xea, (byte) 0xaa, (byte) 0xfe };
        BidibMessage bidibMessage = ResponseFactory.create(message);

        Assert.assertNotNull(bidibMessage);
        Assert.assertTrue(bidibMessage instanceof NodeNewResponse, "Expected a NodeNewResponse message.");

        // 09:07:38.148 [INFO] org.bidib.jbidibc.message.NodeTabResponse [main] - Created new node:
        // Node[version=1,addr=[0],uniqueId=0xd2000d68000036]
        // Node parentNode = new Node(1, new byte[] { 0 }, NodeUtilsTest.prepareUniqueid("d2000d68000036"));

        NodeNewResponse nodeNewResponse = (NodeNewResponse) bidibMessage;
        Node node = nodeNewResponse.getNode(bidibMessage.getAddr());
        Assert.assertNotNull(node);
        Assert.assertEquals(node.getAddr(), new byte[] { 1 });
    }

    @Test
    public void createValidNodeNewResponse2Message() throws ProtocolException {
        // 09:34:34.047 [DEBUG] org.bidib.jbidibc.MessageReceiver [Thread-9] - Received raw message: 0c 00 21 8d 06 01
        // 05 00 0d 6b 00 69 ea aa fe
        // 21:55:23.149 [DEBUG] org.bidib.jbidibc.MessageReceiver [Thread-9] - Received raw message: 0d 01 00 01 8d 06
        // 01 42 00 0d 67 00 65 ea fb fe
        // 15:03:15.910 [DEBUG] org.bidib.jbidibc.MessageReceiver [Thread-9] - Received raw message: 0d 01 00 03 89 02
        // 00 81 00 0d 72 00 1e 00 16 fe

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

        // 09:07:38.148 [INFO] org.bidib.jbidibc.message.NodeTabResponse [main] - Created new node:
        // Node[version=1,addr=[0],uniqueId=0xd2000d68000036]
        // Node parentNode = new Node(1, new byte[] { 0 }, NodeUtilsTest.prepareUniqueid("d2000d68000036"));

        NodeNewResponse nodeNewResponse = (NodeNewResponse) bidibMessage;
        Node node = nodeNewResponse.getNode(bidibMessage.getAddr());
        Assert.assertNotNull(node);
        Assert.assertEquals(node.getAddr(), new byte[] { 1, 1 });
    }

    // [20130817165457078] Input : |0C 00 7C 8D 02 01 81 00 0D 72 00 1F 00 45 FE |
    // [20130817165457109] IN --- : MSG_NODE_NEW 0C 00 7C 8D 02 01 81 00 0D 72 00 1F 00
    // Node Count 2
    // Node added : 81.00.0D.72.00.1F.00 with Adr : 1.0.0.0
    //
    // [20130817165457531] Input : |0D 01 00 06 89 01 00 81 00 0D 72 00 1F 00 8D FE |
    //
    // [20130817165509375] Input : |0D 01 00 14 8D 02 01 42 00 0D 67 00 65 EA 16 FE |
    // [20130817165509406] IN --- : MSG_NODE_NEW 0D 01 00 14 8D 02 01 42 00 0D 67 00 65 EA
    // Node Count 3
    // Node added : 42.00.0D.67.00.65.EA with Adr : 1.1.0.0

    @Test
    public void createValidNodeTabResponse3Message() throws ProtocolException {
        // 14.08.2013 21:55:23.149: receive NodeNewResponse[[1, 0],num=1,type=141,data=[6, 1, 66, 0, 13, 103, 0, 101,
        // 234]] : 0d 01 00 01 8d 06 01 42 00 0d 67 00 65 ea

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

    @Test
    public void createValidNodeTabResponse5Message() throws ProtocolException {
        // 29.08.2013 07:00:29.919: receive NodeTabResponse[[2],num=1,type=137,data=[1, 1, 5, 0, 13, 108, 0, 51, 0]] :
        // 0d 02 00 01 89 01 01 05 00 0d 6c 00 33 00

        byte[] message =
            new byte[] { 0x0d, 0x02, 0x00, 0x01, (byte) 0x89, 0x01, 0x01, (byte) 0x05, 0x00, (byte) 0x0d, (byte) 0x6c,
                0x00, 0x33, (byte) 0x00 };

        BidibMessage bidibMessage = ResponseFactory.create(message);

        Assert.assertNotNull(bidibMessage);
        Assert.assertTrue(bidibMessage instanceof NodeTabResponse, "Expected a NodeTabResponse message.");

        // Create new node has finished: Node[version=2,addr=[1, 0],uniqueId=0x81000d72001f00]
        Node parentNode = new Node(2, new byte[] { 2 }, NodeUtilsTest.prepareUniqueid("81000d72001f00"));

        NodeTabResponse nodeTabResponse = (NodeTabResponse) bidibMessage;
        Node node = nodeTabResponse.getNode(parentNode.getAddr());
        Assert.assertNotNull(node);

        Assert.assertEquals(node.getAddr(), new byte[] { 2, 1 });
    }

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

    @Test
    public void createValidBoostCurrentResponseMessage() throws ProtocolException {
        // 04 00 04 B1 2C
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
    public void createBoostStatOffResponseMessage() throws ProtocolException {
        // 04 00 04 B1 2C
        byte[] message = { 0x04, 0x00, 0x0e, (byte) 0xB0, (byte) 0x00 };

        BidibMessage bidibMessage = ResponseFactory.create(message);
        Assert.assertNotNull(bidibMessage);
        Assert.assertTrue(bidibMessage instanceof BoostStatResponse, "Expected a BoostStatResponse message.");

        LOGGER.info("Booster state: {}", ((BoostStatResponse) bidibMessage).getState());
        Assert.assertEquals(((BoostStatResponse) bidibMessage).getState(), BoosterState.OFF);
    }

    @Test
    public void createBoostStatOffNoDccResponseMessage() throws ProtocolException {
        // 04 00 04 B1 2C
        byte[] message = { 0x04, 0x00, 0x11, (byte) 0xB0, (byte) 0x06 };

        BidibMessage bidibMessage = ResponseFactory.create(message);
        Assert.assertNotNull(bidibMessage);
        Assert.assertTrue(bidibMessage instanceof BoostStatResponse, "Expected a BoostStatResponse message.");

        LOGGER.info("Booster state: {}", ((BoostStatResponse) bidibMessage).getState());
        Assert.assertEquals(((BoostStatResponse) bidibMessage).getState(), BoosterState.OFF_NO_DCC);
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

    @Test
    public void createLcMacroStateResponse1FromByteArray() throws ProtocolException {
        byte[] message = { 0x06, 0x01, 0x00, (byte) 0xd4, (byte) 0xc8, 0x00, (byte) 0xfc };

        BidibMessage bidibMessage = ResponseFactory.create(message);
        Assert.assertNotNull(bidibMessage);
        Assert.assertEquals(ByteUtils.getInt(bidibMessage.getType()), BidibLibrary.MSG_LC_MACRO_STATE);

        LcMacroStateResponse lcMacroStateResponse = (LcMacroStateResponse) bidibMessage;
        LOGGER.info("lcMacroStateResponse: {}", lcMacroStateResponse);
        Assert.assertEquals(lcMacroStateResponse.getMacroNumber(), 0);

        LcMacroState lcMacroState = lcMacroStateResponse.getMacroState();
        Assert.assertNotNull(lcMacroState);
        LOGGER.info("lcMacroState: {}", lcMacroState);

        Assert.assertEquals(LcMacroState.valueOf(lcMacroState.getType()), LcMacroState.RESTORE);
    }

    @Test
    public void createLcMacroParaResponse1FromByteArray() throws ProtocolException {
        byte[] message =
            { 0x0a, 0x01, 0x00, (byte) 0xd9, (byte) 0xca, 0x00, 0x03, (byte) 0x3f, (byte) 0xbf, (byte) 0x7f,
                (byte) 0xff };

        BidibMessage bidibMessage = ResponseFactory.create(message);
        Assert.assertNotNull(bidibMessage);
        Assert.assertEquals(ByteUtils.getInt(bidibMessage.getType()), BidibLibrary.MSG_LC_MACRO_PARA);

        // start clock
        LcMacroParaResponse lcMacroParaResponse = (LcMacroParaResponse) bidibMessage;
        LOGGER.info("lcMacroParaResponse: {}", lcMacroParaResponse);
        Assert.assertEquals(lcMacroParaResponse.getMacroNumber(), 0);
        Assert.assertEquals(lcMacroParaResponse.getParameterIndex(), 3);
        Assert.assertNotNull(lcMacroParaResponse.getLcMacroParaValue());
        Assert.assertEquals(lcMacroParaResponse.getLcMacroParaValue().getValue(), new byte[] { 63, (byte) 191, 127,
            (byte) 255 });
    }

    @Test
    public void createNewDecoderResponseFromByteArray() throws ProtocolException {
        byte[] message =
            { 0x0a, 0x01, 0x00, (byte) 0xd9, (byte) 0xb3, 0x01, 0x0D, (byte) 0x12, (byte) 0x34, (byte) 0x56,
                (byte) 0x78 };

        BidibMessage bidibMessage = ResponseFactory.create(message);
        Assert.assertNotNull(bidibMessage);
        Assert.assertEquals(ByteUtils.getInt(bidibMessage.getType()), BidibLibrary.MSG_NEW_DECODER);

        NewDecoderResponse response = (NewDecoderResponse) bidibMessage;

        LOGGER.debug("detector address: {}, decoder vid: {}, decoder serial id: {}",
            response.getLocalDetectorAddress(), response.getDecoderVendorId(), response.getDecoderSerialId());

        Assert.assertEquals(response.getDecoderSerialId(), 0x78563412);
        Assert.assertEquals(response.getDecoderVendorId(), 0x0D);
        Assert.assertEquals(response.getLocalDetectorAddress(), 1);
    }

    @Test
    public void createIdSearchAckResponseFromByteArray() throws ProtocolException {
        byte[] message =
            { 0x0f, 0x01, 0x00, (byte) 0xd9, (byte) 0xb4, 0x01, 0x0D, (byte) 0x12, (byte) 0x34, (byte) 0x56,
                (byte) 0x78, 0x0D, (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78 };

        BidibMessage bidibMessage = ResponseFactory.create(message);
        Assert.assertNotNull(bidibMessage);
        Assert.assertEquals(ByteUtils.getInt(bidibMessage.getType()), BidibLibrary.MSG_ID_SEARCH_ACK);

        IdSearchAckResponse response = (IdSearchAckResponse) bidibMessage;

        LOGGER.debug("detector address: {}, decoder vid: {}, decoder sid: {}, search sid: {}",
            response.getLocalDetectorAddress(), response.getDecoderVendorId(), response.getDecoderSerialId(),
            String.format("%08X", response.getSearchSerialId()));

        Assert.assertEquals(response.getSearchSerialId(), 0x78563412);
        Assert.assertEquals(response.getSearchVendorId(), 0x0D);
        Assert.assertEquals(response.getDecoderSerialId(), 0x78563412);
        Assert.assertEquals(response.getDecoderVendorId(), 0x0D);
        Assert.assertEquals(response.getLocalDetectorAddress(), 1);
    }

    @Test
    public void createAddrChangeAckResponseFromByteArray() throws ProtocolException {
        byte[] message =
            { 0x0c, 0x01, 0x00, (byte) 0xd9, (byte) 0xb5, 0x01, 0x0D, (byte) 0x12, (byte) 0x34, (byte) 0x56,
                (byte) 0x78, 0x12, (byte) 0x34 };

        BidibMessage bidibMessage = ResponseFactory.create(message);
        Assert.assertNotNull(bidibMessage);
        Assert.assertEquals(ByteUtils.getInt(bidibMessage.getType()), BidibLibrary.MSG_ADDR_CHANGE_ACK);

        AddrChangeAckResponse response = (AddrChangeAckResponse) bidibMessage;

        LOGGER.debug("detector address: {}, decoder vid: {}, decoder sid: {}, new address: {}",
            response.getLocalDetectorAddress(), response.getDecoderVendorId(),
            String.format("%08X", response.getDecoderSerialId()), String.format("%04X", response.getNewAddress()));

        Assert.assertEquals(response.getNewAddress(), 0x3412);
        Assert.assertEquals(response.getDecoderSerialId(), 0x78563412);
        Assert.assertEquals(response.getDecoderVendorId(), 0x0D);
        Assert.assertEquals(response.getLocalDetectorAddress(), 1);
    }

    @Test
    public void createStringResponseMessage() throws ProtocolException {
        byte[] message =
            { 0x0B, 0x00, 0x00, (byte) BidibLibrary.MSG_STRING, (byte) 0x00, (byte) 0x00, 0x05, 0x42, 0x69, 0x44, 0x69,
                0x42 };

        BidibMessage bidibMessage = ResponseFactory.create(message);

        Assert.assertNotNull(bidibMessage);
        Assert.assertTrue(bidibMessage instanceof StringResponse, "Expected a StringResponse message.");
        Assert.assertEquals(bidibMessage.getType(), (byte) BidibLibrary.MSG_STRING);
        StringResponse stringResponse = (StringResponse) bidibMessage;
        Assert.assertNotNull(stringResponse.getStringData());
        StringData stringData = stringResponse.getStringData();

        Assert.assertEquals(stringData.getNamespace(), 0);
        Assert.assertEquals(stringData.getIndex(), 0);
        Assert.assertEquals(stringData.getValue(), "BiDiB");
    }
}
