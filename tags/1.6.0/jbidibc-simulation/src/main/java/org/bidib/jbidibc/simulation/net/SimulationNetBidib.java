package org.bidib.jbidibc.simulation.net;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.DatagramSocket;
import java.util.LinkedList;
import java.util.List;

import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.net.NetBidib;
import org.bidib.jbidibc.net.NetBidibPort;
import org.bidib.jbidibc.net.NetMessageHandler;
import org.bidib.jbidibc.simulation.SimulationInterface;
import org.bidib.jbidibc.simulation.SimulatorRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulationNetBidib extends NetBidib implements SimulationInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationNetBidib.class);

    private Thread portWorker;

    private NetMessageHandler netSimulationMessageHandler;

    private NetBidibPort netBidibPortSimulator;

    private SimulatorRegistry simulatorRegistry;

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

    protected SimulatorRegistry getSimulatorRegistry() {
        return simulatorRegistry;
    }

    @Override
    public void start(String simulationConfigurationLocation) {
        LOGGER.info("Start the simulator.");

        // create the simulation components
        try {
            LOGGER.info("Create a datagram socket with the portnumber: {}", NetBidib.BIDIB_UDP_PORT_NUMBER);
            DatagramSocket datagramSocket = new DatagramSocket(NetBidib.BIDIB_UDP_PORT_NUMBER);

            // create the message receiver that handles incoming commands from the host and forward the commands to the
            // simulators
            netSimulationMessageHandler = createSimulationMessageHandler(simulationConfigurationLocation);

            // open the port that simulates the interface device
            netBidibPortSimulator = new NetBidibPort(datagramSocket, netSimulationMessageHandler);

            LOGGER.info("Prepare and start the port worker.");

            portWorker = new Thread(netBidibPortSimulator);
            portWorker.start();
        }
        catch (Exception ex) {
            LOGGER.warn("Start the simulator failed.", ex);
        }
    }

    protected NetMessageHandler createSimulationMessageHandler(String simulationConfigurationLocation) {
        // load the SimulatorRegistry with the simulation configuration
        simulatorRegistry = SimulatorRegistry.getInstance();
        simulatorRegistry.removeAll();

        // create the message handler that delegates the incoming messages to the message receiver that has a
        // simulator node configured
        SimulationMessageReceiver simulationMessageReceiver = new SimulationMessageReceiver() {
            @Override
            public void publishResponse(ByteArrayOutputStream output) throws ProtocolException {

                // Publish the responses to the host
                LOGGER.info(
                    "Publish the response. Prepare message to send to host using netSimulationMessageHandler: {}",
                    netSimulationMessageHandler);
                try {
                    // TODO add the UDP packet wrapper ...
                    netSimulationMessageHandler.send(netBidibPortSimulator, output.toByteArray());
                }
                catch (Exception ex) {
                    LOGGER.warn("Process messages failed.", ex);
                }
            }
        };
        simulationMessageReceiver.setSimulatorRegistry(simulatorRegistry);

        LOGGER.info("Load simulationConfigurationLocation from: {}", simulationConfigurationLocation);
        // String path = getClass().getResource(simulationConfigurationLocation).getPath();
        // File simulationConfiguration = new File(path);
        InputStream simulationConfiguration = getClass().getResourceAsStream(simulationConfigurationLocation);
        simulatorRegistry.loadSimulationConfiguration(simulationConfiguration, simulationMessageReceiver);

        LOGGER.info("Simulator registry is loaded.");

        SimulationNetMessageHandler netMessageHandler = new SimulationNetMessageHandler(simulationMessageReceiver);
        LOGGER.info("Created the simulation netMessageHandler: {}", netMessageHandler);
        return netMessageHandler;
    }

    @Override
    public void stop() {
        LOGGER.info("Stop the simulator.");

        if (netBidibPortSimulator != null) {
            LOGGER.info("Stop the port.");
            netBidibPortSimulator.stop();

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

            netBidibPortSimulator = null;
        }

        LOGGER.info("Stop the simulator finished.");
    }

    @Override
    public List<String> getPortIdentifiers() {
        List<String> portIdentifiers = new LinkedList<>();
        portIdentifiers.add("mock");
        return portIdentifiers;
    }
}
