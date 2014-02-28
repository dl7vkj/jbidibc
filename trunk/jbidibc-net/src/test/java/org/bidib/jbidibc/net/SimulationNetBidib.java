package org.bidib.jbidibc.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.List;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.message.BidibCommand;
import org.bidib.jbidibc.message.RequestFactory;
import org.bidib.jbidibc.utils.ByteUtils;
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

            LOGGER.info("address: {}, port: {}, data: {}", packet.getAddress(), packet.getPort(),
                ByteUtils.bytesToHex(packet.getData(), packet.getLength()));

            // remove the UDP paket wrapper data and forward to the MessageReceiver
            byte[] messages = new byte[packet.getLength() - 4];

            System.arraycopy(packet.getData(), 4, messages, 0, messages.length);

            // if a CRC error is detected in splitMessages the reading loop will terminate ...
            try {

                List<BidibCommand> commands = new RequestFactory().create(messages);

                if (commands != null) {

                    for (BidibCommand bidibCommand : commands) {

                        switch (bidibCommand.getType()) {
                            case BidibLibrary.MSG_SYS_GET_MAGIC:
                                LOGGER.info("Received get magic message.");
                                // TODO send a response
                                break;
                            default:
                                LOGGER.warn("BidibCommand is not handled: {}", bidibCommand);
                                break;
                        }
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
