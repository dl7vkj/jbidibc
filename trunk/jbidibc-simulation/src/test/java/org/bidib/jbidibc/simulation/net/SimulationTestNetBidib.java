package org.bidib.jbidibc.simulation.net;

import java.io.ByteArrayOutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.bidib.jbidibc.core.BidibMessageProcessor;
import org.bidib.jbidibc.core.Context;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.net.NetBidib;
import org.bidib.jbidibc.net.NetBidibPort;
import org.bidib.jbidibc.net.NetMessageReceiver;
import org.bidib.jbidibc.simulation.SimulatorNode;
import org.bidib.jbidibc.simulation.nodes.DefaultNodeSimulator;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulationTestNetBidib {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationTestNetBidib.class);

    private Thread portWorker;

    private NetMessageReceiver messageReceiver;

    private NetBidibPort netBidibPort;

    private SimulatorNode simulatorNode;

    public SimulationTestNetBidib() {

    }

    private int sessionKey;

    private int sequence;

    public void start() {
        LOGGER.info("Start the simulator.");

        // prepare the default simulator
        simulatorNode = new DefaultNodeSimulator(new byte[] { 0 }, 0xd2000d680064eaL, new BidibMessageProcessor() {

            @Override
            public void processMessages(final Context context, ByteArrayOutputStream output) throws ProtocolException {
                // TODO Publish the responses to the host

                LOGGER.info("Prepare message to send to host, context: {}", context);

                try {
                    // TODO add the UDP packet wrapper ...
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bos.write(ByteUtils.getHighByte(sessionKey));
                    bos.write(ByteUtils.getLowByte(sessionKey));
                    bos.write(ByteUtils.getHighByte(sequence));
                    bos.write(ByteUtils.getLowByte(sequence));
                    output.writeTo(bos);

                    InetAddress address = (InetAddress) context.getParam("foreignAddress");
                    int portNumber = (int) context.getParam("foreignPort");
                    LOGGER.info("Send message to address: {}, portNumber: {}", address, portNumber);

                    netBidibPort.send(bos.toByteArray(), address, portNumber);
                }
                catch (Exception ex) {
                    LOGGER.warn("Process messages failed.", ex);
                }
            }
        });
        // start the simulator
        simulatorNode.start();

        try {
            DatagramSocket datagramSocket = new DatagramSocket(NetBidib.BIDIB_UDP_PORT_NUMBER);

            messageReceiver = createMessageReceiver();

            // open the port
            netBidibPort = new NetBidibPort(datagramSocket, messageReceiver);

            LOGGER.info("Prepare and start the port worker.");

            portWorker = new Thread(netBidibPort);
            portWorker.start();
        }
        catch (Exception ex) {
            LOGGER.warn("Start the simulator failed.", ex);
        }
    }

    protected NetMessageReceiver createMessageReceiver() {
        return new SimulationNetMessageReceiver(simulatorNode);
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

    public static void main(String[] args) {
        new SimulationNetBidib().start();
    }
}
