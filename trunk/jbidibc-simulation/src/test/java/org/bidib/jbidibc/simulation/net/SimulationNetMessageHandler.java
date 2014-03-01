package org.bidib.jbidibc.simulation.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.message.BidibCommand;
import org.bidib.jbidibc.message.RequestFactory;
import org.bidib.jbidibc.net.NetBidibPort;
import org.bidib.jbidibc.net.NetMessageHandler;
import org.bidib.jbidibc.simulation.SimulatorNode;
import org.bidib.jbidibc.utils.ByteUtils;
import org.bidib.jbidibc.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO pending refatoring: Use the DefaultNetMessageHandler and add a MessageReceiver that uses the simulatorNode
public class SimulationNetMessageHandler implements NetMessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationNetMessageHandler.class);

    private SimulatorNode simulatorNode;

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

    public SimulationNetMessageHandler(SimulatorNode simulatorNode) {
        LOGGER.info("Create new instance of SimulationNetMessageReceiver with simulatorNode: {}", simulatorNode);
        this.simulatorNode = simulatorNode;
    }

    @Override
    public void addRemoteAddress(InetAddress address, int port) {
        LOGGER.info("Add remote address: {}, port: {}", address, port);
        KnownBidibHost knownBidibHost = new KnownBidibHost(address, port);
        knownBidibHosts.add(knownBidibHost);
    }

    @Override
    public void receive(final DatagramPacket packet) {
        LOGGER.info("Received packet, foreign address: {}, foreign port: {}, data: {}", packet.getAddress(),
            packet.getPort(), ByteUtils.bytesToHex(packet.getData(), packet.getLength()));

        KnownBidibHost current = new KnownBidibHost(packet.getAddress(), packet.getPort());
        if (!knownBidibHosts.contains(current)) {
            knownBidibHosts.add(current);
        }

        // TODO check if remove the 4 bytes is correct here ...

        // remove the UDP paket wrapper data and forward to the MessageReceiver
        byte[] messages = new byte[packet.getLength() - 4];

        System.arraycopy(packet.getData(), 4, messages, 0, messages.length);

        // if a CRC error is detected in splitMessages the reading loop will terminate ...
        try {
            List<BidibCommand> commands = new RequestFactory().create(messages);

            if (commands != null) {

                for (BidibCommand bidibCommand : commands) {
                    LOGGER.info("Process the current bidibCommand: {}", bidibCommand);

                    simulatorNode.processRequest(bidibCommand);
                }
            }
            else {
                LOGGER.warn("No commands in packet received.");
            }
        }
        catch (ProtocolException ex) {
            LOGGER.warn("Create BiDiB message failed.", ex);
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
