package org.bidib.jbidibc.simulation.comm;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import org.bidib.jbidibc.net.NetBidib;
import org.bidib.jbidibc.net.NetBidibPort;
import org.bidib.jbidibc.net.NetMessageReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulationNetBidib {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationNetBidib.class);

    private Thread portWorker;

    private NetMessageReceiver messageReceiver;

    private NetBidibPort netBidibPort;

    private final class SimulationNetMessageReceiver implements NetMessageReceiver {

        @Override
        public void receive(DatagramPacket packet) {
            // TODO Auto-generated method stub
            LOGGER.info("Received packet: {}", packet);

            LOGGER.info("address: {}, port: {}", packet.getAddress(), packet.getPort());
        }
    }

    public SimulationNetBidib() {

    }

    public void start() {
        LOGGER.info("Start the simulator.");
        try {
            DatagramSocket datagramSocket = new DatagramSocket(NetBidib.BIDIB_UDP_PORT_NUMBER);

            messageReceiver = new SimulationNetMessageReceiver();

            // open the port
            netBidibPort = new NetBidibPort(datagramSocket, (NetMessageReceiver) messageReceiver);

            LOGGER.info("Prepare and start the port worker.");

            portWorker = new Thread(netBidibPort);
            portWorker.start();
        }
        catch (Exception ex) {
            LOGGER.warn("Start the simulator failed.", ex);
        }
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
        LOGGER.info("Stop the simulator finished.");
    }

    public static void main(String[] args) {
        new SimulationNetBidib().start();
    }
}
