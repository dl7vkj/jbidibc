package org.bidib.jbidibc;

import gnu.io.SerialPort;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.bidib.jbidibc.enumeration.BoosterState;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.message.BidibMessage;
import org.bidib.jbidibc.message.ResponseFactory;
import org.bidib.jbidibc.node.BidibNode;
import org.bidib.jbidibc.node.NodeFactory;
import org.mockito.Mockito;
import org.testng.annotations.Test;

public class MessageReceiverTest {

    @Test
    public void receiveMessageTest() throws IOException {
        byte[] address = new byte[] { 0 };

        NodeFactory nodeFactory = Mockito.mock(NodeFactory.class);
        SerialPort serialPort = Mockito.mock(SerialPort.class);
        BidibNode bidibNode = Mockito.mock(BidibNode.class);
        MessageListener messageListener = Mockito.mock(MessageListener.class);

        // prepare the object to test
        MessageReceiver receiver = new MessageReceiver(nodeFactory);
        receiver.addMessageListener(messageListener);

        ByteArrayInputStream is =
            new ByteArrayInputStream(new byte[] { 0x05, 0x00, 0x01, (byte) 0x86, (byte) 0x02, (byte) 0x00, (byte) 0x46,
                (byte) 0xFE });

        Mockito.when(serialPort.getInputStream()).thenReturn(is);
        Mockito.when(nodeFactory.getNode(Mockito.any(Node.class))).thenReturn(bidibNode);
        Mockito.when(bidibNode.getNextReceiveMsgNum(Mockito.any(BidibMessage.class))).thenReturn(Integer.valueOf(1));

        receiver.receive(serialPort);

        // verify that error was called once
        Mockito.verify(messageListener, Mockito.times(1)).error(address, 2);
    }

    @Test
    public void receiveFeedbackMultipleResponseOccupiedTest() throws IOException {

        byte[] address = new byte[] { 1, 0 };

        NodeFactory nodeFactory = Mockito.mock(NodeFactory.class);
        SerialPort serialPort = Mockito.mock(SerialPort.class);
        BidibNode bidibNode = Mockito.mock(BidibNode.class);
        MessageListener messageListener = Mockito.mock(MessageListener.class);

        // prepare the object to test
        MessageReceiver receiver = new MessageReceiver(nodeFactory);
        receiver.addMessageListener(messageListener);

        ByteArrayInputStream is =
            new ByteArrayInputStream(new byte[] { (byte) 0xfe, 0x16, (byte) 0x01, (byte) 0x00, (byte) 0x06,
                (byte) 0xa2, (byte) 0x00, (byte) 0x80, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xfe });

        Mockito.when(serialPort.getInputStream()).thenReturn(is);
        Mockito.when(nodeFactory.getNode(Mockito.any(Node.class))).thenReturn(bidibNode);
        Mockito.when(bidibNode.getNextReceiveMsgNum(Mockito.any(BidibMessage.class))).thenReturn(Integer.valueOf(6));

        receiver.receive(serialPort);

        // verify that only occupied was called once
        Mockito.verify(messageListener, Mockito.never()).free(address, 2);
        // feedback 2 is occupied
        Mockito.verify(messageListener, Mockito.times(1)).occupied(address, 2);
    }

    @Test
    public void receiveFeedbackMultipleResponseFreeTest() throws IOException {

        byte[] address = new byte[] { 1, 0 };

        NodeFactory nodeFactory = Mockito.mock(NodeFactory.class);
        SerialPort serialPort = Mockito.mock(SerialPort.class);
        BidibNode bidibNode = Mockito.mock(BidibNode.class);
        MessageListener messageListener = Mockito.mock(MessageListener.class);

        // prepare the object to test
        MessageReceiver receiver = new MessageReceiver(nodeFactory);
        receiver.addMessageListener(messageListener);

        ByteArrayInputStream is =
            new ByteArrayInputStream(new byte[] { (byte) 0xfe, 0x16, (byte) 0x01, (byte) 0x00, (byte) 0x06,
                (byte) 0xa2, (byte) 0x00, (byte) 0x80, (byte) 0x00, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xfe });

        Mockito.when(serialPort.getInputStream()).thenReturn(is);
        Mockito.when(nodeFactory.getNode(Mockito.any(Node.class))).thenReturn(bidibNode);
        Mockito.when(bidibNode.getNextReceiveMsgNum(Mockito.any(BidibMessage.class))).thenReturn(Integer.valueOf(6));

        receiver.receive(serialPort);

        // verify that only occupied was called once
        Mockito.verify(messageListener, Mockito.never()).occupied(address, 2);
        // feedback 0 is free
        Mockito.verify(messageListener, Mockito.times(1)).free(address, 0);
        // feedback 2 is free
        Mockito.verify(messageListener, Mockito.times(1)).free(address, 2);
        // feedback 7 is free
        Mockito.verify(messageListener, Mockito.times(1)).free(address, 7);
        // feedback 8 is occupied
        Mockito.verify(messageListener, Mockito.times(1)).occupied(address, 8);
    }

    @Test
    public void receive2MessagesTest() throws IOException, ProtocolException {

        BidibMessage featureResponse1 =
            ResponseFactory.create(new byte[] { 0x06, 0x01, 0x00, 0x0e, (byte) 0x90, 0x00, 0x20 });

        NodeFactory nodeFactory = Mockito.mock(NodeFactory.class);
        SerialPort serialPort = Mockito.mock(SerialPort.class);
        BidibNode bidibNode = Mockito.mock(BidibNode.class);
        MessageListener messageListener = Mockito.mock(MessageListener.class);

        MessageReceiver receiver = new MessageReceiver(nodeFactory);
        receiver.addMessageListener(messageListener);

        ByteArrayInputStream is =
            new ByteArrayInputStream(new byte[] { 0x06, 0x01, 0x00, 0x0e, (byte) 0x90, 0x00, 0x20, /*CRC*/(byte) 0xA7,
                (byte) 0xFE, /*start 2nd message*/0x06, 0x01, 0x00, 0x0f, (byte) 0x90, 0x00, 0x20, /*CRC*/
                (byte) 0x28, (byte) 0xFE });

        Mockito.when(serialPort.getInputStream()).thenReturn(is);
        Mockito.when(nodeFactory.getNode(Mockito.any(Node.class))).thenReturn(bidibNode);
        Mockito.when(bidibNode.getNextReceiveMsgNum(Mockito.any(BidibMessage.class))).thenReturn(Integer.valueOf(0x0f));
        Mockito.when(bidibNode.getNextReceiveMsgNum(featureResponse1)).thenReturn(Integer.valueOf(0x0e));

        receiver.receive(serialPort);

        // verify that no message listener methods were called.
        Mockito.verifyZeroInteractions(messageListener);
    }

    @Test
    public void receiveBoostStatResponseTest() throws IOException {
        byte[] address = new byte[] { 0 };

        NodeFactory nodeFactory = Mockito.mock(NodeFactory.class);
        SerialPort serialPort = Mockito.mock(SerialPort.class);
        BidibNode bidibNode = Mockito.mock(BidibNode.class);
        MessageListener messageListener = Mockito.mock(MessageListener.class);

        MessageReceiver receiver = new MessageReceiver(nodeFactory);
        receiver.addMessageListener(messageListener);

        ByteArrayInputStream is =
            new ByteArrayInputStream(
                new byte[] { 0x04, 0x00, 0x3a, (byte) 0xb0, (byte) 0x06, (byte) 0x74, (byte) 0xFE });

        Mockito.when(serialPort.getInputStream()).thenReturn(is);
        Mockito.when(nodeFactory.getNode(Mockito.any(Node.class))).thenReturn(bidibNode);
        Mockito.when(bidibNode.getNextReceiveMsgNum(Mockito.any(BidibMessage.class))).thenReturn(Integer.valueOf(58));

        receiver.receive(serialPort);

        // verify that booster state is on
        Mockito.verify(messageListener, Mockito.never()).boosterState(address, BoosterState.ON);
    }
}
