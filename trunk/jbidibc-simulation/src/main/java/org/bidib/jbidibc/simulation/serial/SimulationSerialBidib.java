package org.bidib.jbidibc.simulation.serial;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bidib.jbidibc.ConnectionListener;
import org.bidib.jbidibc.MessageListener;
import org.bidib.jbidibc.MessageReceiver;
import org.bidib.jbidibc.NodeListener;
import org.bidib.jbidibc.core.AbstractBidib;
import org.bidib.jbidibc.core.BidibMessageProcessor;
import org.bidib.jbidibc.exception.PortNotFoundException;
import org.bidib.jbidibc.exception.PortNotOpenedException;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.message.BidibCommand;
import org.bidib.jbidibc.message.BidibMessage;
import org.bidib.jbidibc.message.RequestFactory;
import org.bidib.jbidibc.node.NodeFactory;
import org.bidib.jbidibc.node.listener.TransferListener;
import org.bidib.jbidibc.simulation.SimulationInterface;
import org.bidib.jbidibc.simulation.SimulatorNode;
import org.bidib.jbidibc.simulation.SimulatorRegistry;
import org.bidib.jbidibc.simulation.net.SimulationBidibMessageProcessor;
import org.bidib.jbidibc.simulation.net.SimulationMessageReceiver;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulationSerialBidib extends AbstractBidib implements SimulationInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationSerialBidib.class);

    // private BidibMessageProcessor messageReceiver;

    // private BidibMessageProcessor getMessageReceiver() {
    // return messageReceiver;
    // }

    private String requestedPortName;

    /*
     * This method is called from open().
     */
    @Override
    protected BidibMessageProcessor createMessageReceiver(NodeFactory nodeFactory) {
        return new MessageReceiver(
            nodeFactory) {
        };
    }

    @Override
    public void start(String simulationConfigurationLocation) {
        LOGGER.info("Start the simulator.");

        // TODO load the SimulatorRegistry with the simulation configuration
        SimulatorRegistry.getInstance().removeAll();

        String path = getClass().getResource(simulationConfigurationLocation).getPath();
        File simulationConfiguration = new File(path);
        SimulatorRegistry.getInstance().loadSimulationConfiguration(simulationConfiguration,
            (SimulationBidibMessageProcessor) this.getSimulationMessageReceiver());
    }

    private SimulationBidibMessageProcessor getSimulationMessageReceiver() {
        return new SimulationMessageReceiver() {
            @Override
            public void publishResponse(ByteArrayOutputStream output) throws ProtocolException {
                LOGGER.info("Publish the response from the SimulationMessageReceiver to the messageReceiver.");

                getMessageReceiver().processMessages(output);
            }
        };
    }

    @Override
    public void stop() {

    }

    @Override
    public void send(byte[] bytes) {
        LOGGER.info("Send is called with bytes: {}", ByteUtils.bytesToHex(bytes));

        try {
            // TODO change the call to request factory
            List<BidibCommand> bidibMessages = new RequestFactory().create(bytes);
            for (BidibCommand bidibMessage : bidibMessages) {
                byte[] address = ((BidibMessage) bidibMessage).getAddr();

                String nodeAddress = ByteUtils.bytesToHex(address);

                SimulatorNode simulator = SimulatorRegistry.getInstance().getSimulator(nodeAddress);
                if (simulator == null) {
                    LOGGER.warn("No simulator found for nodeAddress: {}", nodeAddress);
                }

                simulator.processRequest(bidibMessage);

                LOGGER.debug("Forwarded message to simulator: {}", bidibMessage);
            }
        }
        catch (Exception ex) {
            LOGGER.warn("Process request failed.", ex);
        }
    }

    @Override
    public void open(
        String portName, ConnectionListener connectionListener, Set<NodeListener> nodeListeners,
        Set<MessageListener> messageListeners, Set<TransferListener> transferListeners) throws PortNotFoundException,
        PortNotOpenedException {
        // TODO Auto-generated method stub
        setConnectionListener(connectionListener);
        registerListeners(nodeListeners, messageListeners, transferListeners);
    }

    @Override
    public boolean isOpened() {
        return isOpened;
    }

    private boolean isOpened;

    @Override
    public void close() {

        // TODO Auto-generated method stub
        isOpened = false;

        if (getConnectionListener() != null) {
            getConnectionListener().closed(requestedPortName);
        }

        requestedPortName = null;

        // if (simulationInterface != null) {
        // simulationInterface.stop();
        // }

        // TODO remove the simulation
        SimulatorRegistry.getInstance().removeAll();
    }

    @Override
    public void setLogFile(String logFile) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<String> getPortIdentifiers() {
        List<String> portIdentifiers = new LinkedList<>();
        portIdentifiers.add("mock");
        return portIdentifiers;
    }
}
