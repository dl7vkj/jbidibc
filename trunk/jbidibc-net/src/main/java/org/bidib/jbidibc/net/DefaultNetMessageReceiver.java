package org.bidib.jbidibc.net;

import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;

import org.bidib.jbidibc.MessageReceiver;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.node.NodeFactory;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultNetMessageReceiver extends MessageReceiver implements NetMessageReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultNetMessageReceiver.class);

    protected DefaultNetMessageReceiver(NodeFactory nodeFactory) {
        super(nodeFactory);
    }

    @Override
    public void receive(final DatagramPacket packet) {
        LOGGER.info("Received a packet: {}", packet);

        // TODO a datagramm packet was received ... process the envelope and extract the message
        LOGGER.info("address: {}, port: {}, data: {}", packet.getAddress(), packet.getPort(),
            ByteUtils.bytesToHex(packet.getData(), packet.getLength()));

        // byte[] messages = new byte[packet.getLength() - 4];
        //
        // System.arraycopy(packet.getData(), 4, messages, 0, messages.length);

        // TODO for the first magic response we need special processing because we need to keep the session key

        // remove the UDP paket wrapper data and forward to the MessageReceiver
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(packet.getData(), 4, packet.getLength() - 4);

        try {
            processMessages(null, output);
        }
        catch (ProtocolException ex) {
            LOGGER.warn("Process messages failed.", ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public byte[] getRemainingOutputBuffer() {
        // TODO Auto-generated method stub
        return null;
    }

}
