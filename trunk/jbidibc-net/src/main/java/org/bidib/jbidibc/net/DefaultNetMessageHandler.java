package org.bidib.jbidibc.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

import org.bidib.jbidibc.core.BidibMessageProcessor;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
import org.bidib.jbidibc.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultNetMessageHandler implements NetMessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultNetMessageHandler.class);

    private BidibMessageProcessor messageReceiverDelegate;

    private int sessionKey;

    private int sequence;

    public final class KnownBidibHost {
        private final InetAddress address;

        private final int portNumber;

        public KnownBidibHost(final InetAddress address, final int portNumber) {
            this.address = address;
            this.portNumber = portNumber;
        }

        /**
         * @return the address
         */
        public InetAddress getAddress() {
            return address;
        }

        /**
         * @return the portNumber
         */
        public int getPortNumber() {
            return portNumber;
        }
    }

    private List<KnownBidibHost> knownBidibHosts = new LinkedList<KnownBidibHost>();

    protected DefaultNetMessageHandler(BidibMessageProcessor messageReceiverDelegate) {
        this.messageReceiverDelegate = messageReceiverDelegate;
    }

    @Override
    public void addRemoteAddress(InetAddress address, int port) {
        LOGGER.info("Add remote address: {}, port: {}", address, port);
        KnownBidibHost knownBidibHost = new KnownBidibHost(address, port);
        knownBidibHosts.add(knownBidibHost);
    }

    @Override
    public void receive(final DatagramPacket packet) {
        LOGGER.info("Received a packet: {}", packet);

        // TODO a datagramm packet was received ... process the envelope and extract the message
        LOGGER.info("address: {}, port: {}, data: {}", packet.getAddress(), packet.getPort(),
            ByteUtils.bytesToHex(packet.getData(), packet.getLength()));

        KnownBidibHost current = new KnownBidibHost(packet.getAddress(), packet.getPort());
        if (!knownBidibHosts.contains(current)) {
            knownBidibHosts.add(current);
        }

        // TODO for the first magic response we need special processing because we need to keep the session key

        // remove the UDP paket wrapper data and forward to the MessageReceiver
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(packet.getData(), 4, packet.getLength() - 4);

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
        LOGGER.info("Send message to port: {}, message: {}", port, ByteUtils.bytesToHex(bytes));

        if (!CollectionUtils.hasElements(knownBidibHosts)) {
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

                for (KnownBidibHost host : knownBidibHosts) {
                    LOGGER.info("Send message to host: {}, address: {}", host.getAddress(), host.getPortNumber());
                    port.send(bos.toByteArray(), host.getAddress(), host.getPortNumber());
                }

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
