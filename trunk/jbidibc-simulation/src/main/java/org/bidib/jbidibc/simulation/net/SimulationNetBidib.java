package org.bidib.jbidibc.simulation.net;

import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.bidib.jbidibc.core.BidibMessageProcessor;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.net.NetBidib;
import org.bidib.jbidibc.net.NetBidibPort;
import org.bidib.jbidibc.net.NetMessageHandler;
import org.bidib.jbidibc.simulation.SimulatorNode;
import org.bidib.jbidibc.simulation.nodes.DefaultNodeSimulator;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulationNetBidib {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationNetBidib.class);

    private Thread portWorker;

    private NetMessageHandler netMessageHandler;

    private NetBidibPort netBidibPort;

    private SimulatorNode simulatorNode;

    private int sessionKey;

    private int sequence;

    public SimulationNetBidib() {

    }

    protected int getSessionKey() {
        return sessionKey;
    }

    protected int getSequence() {
        return sequence;
    }

    protected SimulatorNode getSimulatorNode() {
        return simulatorNode;
    }

    public void start() {
        LOGGER.info("Start the simulator.");

        // prepare the default simulator
        simulatorNode = new DefaultNodeSimulator(new byte[] { 0 }, 0xd2000d680064eaL, new BidibMessageProcessor() {

            @Override
            public void processMessages(ByteArrayOutputStream output) throws ProtocolException {
                // Publish the responses to the host

                LOGGER.info("Prepare message to send to host.");

                try {
                    // // TODO add the UDP packet wrapper ...
                    // ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    // bos.write(ByteUtils.getHighByte(sessionKey));
                    // bos.write(ByteUtils.getLowByte(sessionKey));
                    // bos.write(ByteUtils.getHighByte(sequence));
                    // bos.write(ByteUtils.getLowByte(sequence));
                    // output.writeTo(bos);
                    //
                    // InetAddress address = (InetAddress) context.getParam("foreignAddress");
                    // int portNumber = (int) context.getParam("foreignPort");
                    // LOGGER.info("Send message to address: {}, portNumber: {}", address, portNumber);
                    // netBidibPort.send(bos.toByteArray(), address, portNumber);

                    // TODO test if this is correct ...
                    netMessageHandler.send(netBidibPort, output.toByteArray());
                }
                catch (Exception ex) {
                    LOGGER.warn("Process messages failed.", ex);
                }
            }

            @Override
            public String getErrorInformation() {
                // TODO Auto-generated method stub
                return null;
            }
        });
        // start the simulator
        simulatorNode.start();

        LOGGER.info("Simulator node is started, prepare the datagram socket.");

        try {
            LOGGER.info("Create a datagram socket with the portnumber: {}", NetBidib.BIDIB_UDP_PORT_NUMBER);
            DatagramSocket datagramSocket = new DatagramSocket(NetBidib.BIDIB_UDP_PORT_NUMBER);

            // create the message receiver that handles incoming commands from the host
            netMessageHandler = createMessageHandler();

            // open the port
            netBidibPort = new NetBidibPort(datagramSocket, netMessageHandler);

            LOGGER.info("Prepare and start the port worker.");

            portWorker = new Thread(netBidibPort);
            portWorker.start();
        }
        catch (Exception ex) {
            LOGGER.warn("Start the simulator failed.", ex);
        }
    }

    protected NetMessageHandler createMessageHandler() {

        NetMessageHandler netMessageHandler = new NetMessageHandler() {

            @Override
            public void receive(DatagramPacket packet) {
                LOGGER.warn("### Received packet and do nothing, foreign address: {}, foreign port: {}, data: {}",
                    packet.getAddress(), packet.getPort(), ByteUtils.bytesToHex(packet.getData(), packet.getLength()));
            }

            @Override
            public void send(NetBidibPort port, byte[] bytes) {
                LOGGER.warn("### Send data will do nothing, port: {}, data: {}", port, ByteUtils.bytesToHex(bytes));
            }

            @Override
            public void addRemoteAddress(InetAddress address, int port) {
                LOGGER.warn("### Add remote address is discarded, address: {}, port: {}", address, port);
            }

        };

        return netMessageHandler;
    }

    public void stop() {
        LOGGER.info("Stop the simulator.");

        if (netBidibPort != null) {
            LOGGER.info("Stop the port.");
            netBidibPort.stop();

            if (portWorker != null) {
                synchronized (portWorker) {
                    try {
                        portWorker.join(5000L);
                    }
                    catch (InterruptedException ex) {
                        LOGGER.warn("Wait for termination of port worker failed.", ex);
                    }
                    portWorker = null;
                }
            }

            netBidibPort = null;
        }

        if (simulatorNode != null) {
            simulatorNode.stop();
        }

        LOGGER.info("Stop the simulator finished.");
    }
}
