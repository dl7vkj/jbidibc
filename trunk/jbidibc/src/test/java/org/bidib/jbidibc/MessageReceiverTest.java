package org.bidib.jbidibc;

import gnu.io.SerialPort;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.bidib.jbidibc.message.BidibMessage;
import org.bidib.jbidibc.node.BidibNode;
import org.bidib.jbidibc.node.NodeFactory;
import org.mockito.Mockito;
import org.testng.annotations.Test;

public class MessageReceiverTest {

    @Test
    public void receiveMessageTest() throws IOException {

        NodeFactory nodeFactory = Mockito.mock(NodeFactory.class);
        SerialPort serialPort = Mockito.mock(SerialPort.class);
        BidibNode bidibNode = Mockito.mock(BidibNode.class);

        MessageReceiver receiver = new MessageReceiver(nodeFactory);

        ByteArrayInputStream is =
            new ByteArrayInputStream(new byte[] { 0x05, 0x00, 0x01, (byte) 0x86, (byte) 0x02, (byte) 0x00, (byte) 0x46,
                (byte) 0xFE });

        Mockito.when(serialPort.getInputStream()).thenReturn(is);
        Mockito.when(nodeFactory.getNode(Mockito.any(Node.class))).thenReturn(bidibNode);
        Mockito.when(bidibNode.getNextReceiveMsgNum(Mockito.any(BidibMessage.class))).thenReturn(Integer.valueOf(1));

        receiver.receive(serialPort);

    }
}
