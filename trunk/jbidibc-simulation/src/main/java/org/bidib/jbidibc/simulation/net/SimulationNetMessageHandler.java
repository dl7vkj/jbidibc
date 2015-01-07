package org.bidib.jbidibc.simulation.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.LinkedList;
import java.util.List;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.BidibMessageProcessor;
import org.bidib.jbidibc.core.CRC8;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.bidib.jbidibc.net.BidibNetAddress;
import org.bidib.jbidibc.net.NetBidibPort;
import org.bidib.jbidibc.net.NetMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulationNetMessageHandler implements NetMessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationNetMessageHandler.class);

    private BidibMessageProcessor messageReceiverDelegate;

    // private int sessionKey;
    //
    // private int sequence;

    private List<BidibNetAddress> knownBidibHosts = new LinkedList<BidibNetAddress>();

    private int currentSessionKey;

    public SimulationNetMessageHandler(BidibMessageProcessor messageReceiverDelegate) {
        this.messageReceiverDelegate = messageReceiverDelegate;
    }

    @Override
    public void receive(final DatagramPacket packet) {
        // TODO a datagramm packet was received ... process the envelope and extract the message
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Received a packet from address: {}, port: {}, data: {}", packet.getAddress(),
                packet.getPort(), ByteUtils.bytesToHex(packet.getData(), packet.getLength()));
        }

        BidibNetAddress current = new BidibNetAddress(packet.getAddress(), packet.getPort());
        if (!knownBidibHosts.contains(current)) {
            // TODO: generate a session key and support listen only hosts ...
            currentSessionKey++;
            current.setSessionKey(currentSessionKey);

            LOGGER.info("Adding new known Bidib host: {}", current);
            knownBidibHosts.add(current);
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
    public void send(NetBidibPort port, byte[] message) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Send message to port: {}, message: {}", port, ByteUtils.bytesToHex(message));
        }

        if (port != null) {

            // TODO escape the messages

            try {
                // TODO add the UDP packet wrapper ...
                for (BidibNetAddress host : knownBidibHosts) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bos.write(ByteUtils.getHighByte(host.getSessionKey()));
                    bos.write(ByteUtils.getLowByte(host.getSessionKey()));
                    bos.write(ByteUtils.getHighByte(host.getSequence()));
                    bos.write(ByteUtils.getLowByte(host.getSequence()));

                    // bos.write(bytes);
                    sendDelimiter(bos);

                    byte length = message[0];

                    escape(bos, length);

                    int txCrc = CRC8.getCrcValue(length);

                    for (int i = 1; i <= length; i++) {
                        escape(bos, message[i]);
                        txCrc = CRC8.getCrcValue((message[i] ^ txCrc) & 0xFF);
                    }
                    escape(bos, (byte) txCrc);
                    sendDelimiter(bos);

                    LOGGER.info("Send message to address: {}, port: {}, sessionKey: {}, sequence: {}",
                        host.getAddress(), host.getPortNumber(), host.getSessionKey(), host.getSequence());

                    // increment the sequence counter after sending the message sucessfully
                    host.nextSequence();

                    // send the data to the host
                    port.send(bos.toByteArray(), host.getAddress(), host.getPortNumber());
                }
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

    private void sendDelimiter(ByteArrayOutputStream output) {
        output.write((byte) BidibLibrary.BIDIB_PKT_MAGIC);
    }

    private void escape(ByteArrayOutputStream output, byte c) {
        if ((c == (byte) BidibLibrary.BIDIB_PKT_MAGIC) || (c == (byte) BidibLibrary.BIDIB_PKT_ESCAPE)) {
            output.write((byte) BidibLibrary.BIDIB_PKT_ESCAPE);
            c = (byte) (c ^ 0x20);
        }
        output.write(c);
    }

}
