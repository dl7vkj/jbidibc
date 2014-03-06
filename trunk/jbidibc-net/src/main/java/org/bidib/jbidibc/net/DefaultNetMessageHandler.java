package org.bidib.jbidibc.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import org.bidib.jbidibc.core.BidibMessageProcessor;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultNetMessageHandler implements NetMessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultNetMessageHandler.class);

    private BidibMessageProcessor messageReceiverDelegate;

    private int sessionKey;

    private int sequence;

    private BidibNetAddress remoteAddress;

    public DefaultNetMessageHandler(BidibMessageProcessor messageReceiverDelegate, InetAddress address, int port) {
        this.messageReceiverDelegate = messageReceiverDelegate;

        LOGGER.info("Set the remote address: {}, port: {}", address, port);
        remoteAddress = new BidibNetAddress(address, port);
    }

    @Override
    public void receive(final DatagramPacket packet) {
        // a datagramm packet was received ... process the envelope and extract the message
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Received a packet from address: {}, port: {}, data: {}", packet.getAddress(),
                packet.getPort(), ByteUtils.bytesToHex(packet.getData(), packet.getLength()));
        }
        // TODO for the first magic response we need special processing because we need to keep the session key

        // remove the UDP paket wrapper data and forward to the MessageReceiver
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(packet.getData(), 4, packet.getLength() - 4);

        LOGGER.info("Forward received message to messageReceiverDelegate: {}", messageReceiverDelegate);
        try {
            messageReceiverDelegate.processMessages(output);
        }
        catch (ProtocolException ex) {
            LOGGER.warn("Process messages failed.", ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void send(NetBidibPort port, byte[] bytes) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Send message to port: {}, message: {}", port, ByteUtils.bytesToHex(bytes));
        }

        if (remoteAddress == null) {
            LOGGER.warn("### No remote addresses available. The message will not be sent!");
            return;
        }

        // TODO Auto-generated method stub
        if (port != null) {

            try {
                // TODO add the UDP packet wrapper ...
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bos.write(ByteUtils.getHighByte(sessionKey));
                bos.write(ByteUtils.getLowByte(sessionKey));
                bos.write(ByteUtils.getHighByte(sequence));
                bos.write(ByteUtils.getLowByte(sequence));
                bos.write(bytes);

                LOGGER.info("Send message to remote address, address: {}, port: {}", remoteAddress.getAddress(),
                    remoteAddress.getPortNumber());
                port.send(bos.toByteArray(), remoteAddress.getAddress(), remoteAddress.getPortNumber());

                // increment the sequence counter after sending the message sucessfully
                prepareNextSequence();
            }
            catch (IOException ex) {
                LOGGER.warn("Send message to port failed.", ex);
                throw new RuntimeException("Send message to datagram socket failed.", ex);
            }
        }
        else {
            LOGGER.warn("Send not possible, the port is closed.");
        }
    }

    private void prepareNextSequence() {
        sequence++;
        if (sequence > 65535) {
            sequence = 0;
        }
    }

}
