package org.bidib.jbidibc.simulation.serial;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bidib.jbidibc.core.AbstractBidib;
import org.bidib.jbidibc.core.BidibMessageProcessor;
import org.bidib.jbidibc.core.ConnectionListener;
import org.bidib.jbidibc.core.MessageListener;
import org.bidib.jbidibc.core.MessageReceiver;
import org.bidib.jbidibc.core.NodeListener;
import org.bidib.jbidibc.core.exception.PortNotFoundException;
import org.bidib.jbidibc.core.exception.PortNotOpenedException;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.helpers.Context;
import org.bidib.jbidibc.core.message.BidibCommand;
import org.bidib.jbidibc.core.message.BidibMessage;
import org.bidib.jbidibc.core.message.RequestFactory;
import org.bidib.jbidibc.core.node.NodeFactory;
import org.bidib.jbidibc.core.node.listener.TransferListener;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.bidib.jbidibc.core.utils.NodeUtils;
import org.bidib.jbidibc.simulation.SimulationInterface;
import org.bidib.jbidibc.simulation.SimulatorNode;
import org.bidib.jbidibc.simulation.SimulatorRegistry;
import org.bidib.jbidibc.simulation.net.SimulationBidibMessageProcessor;
import org.bidib.jbidibc.simulation.net.SimulationMessageReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulationSerialBidib extends AbstractBidib implements SimulationInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationSerialBidib.class);

    private SimulationBidibMessageProcessor simulationMessageReceiver;

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
        LOGGER.info("Start the simulator, simulationConfigurationLocation: {}", simulationConfigurationLocation);

        // load the SimulatorRegistry with the simulation configuration
        SimulatorRegistry.getInstance().removeAll();

        InputStream simulationConfiguration = getClass().getResourceAsStream(simulationConfigurationLocation);
        SimulatorRegistry.getInstance().loadSimulationConfiguration(simulationConfiguration,
            (SimulationBidibMessageProcessor) this.getSimulationMessageReceiver());
    }

    private SimulationBidibMessageProcessor getSimulationMessageReceiver() {

        if (simulationMessageReceiver == null) {
            simulationMessageReceiver = new SimulationMessageReceiver() {
                @Override
                public void publishResponse(ByteArrayOutputStream output) throws ProtocolException {
                    LOGGER.info("Publish the response from the SimulationMessageReceiver to the messageReceiver.");

                    getMessageReceiver().processMessages(output);
                }

                @Override
                public void removeNodeListener(NodeListener nodeListener) {
                    getMessageReceiver().removeNodeListener(nodeListener);
                }

                @Override
                public void setIgnoreWrongMessageNumber(boolean ignoreWrongMessageNumber) {
                    LOGGER.info("Set ignoreWrongMessageNumber: {}", ignoreWrongMessageNumber);
                    getMessageReceiver().setIgnoreWrongMessageNumber(ignoreWrongMessageNumber);
                }
            };
        }
        return simulationMessageReceiver;
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

                String nodeAddress = NodeUtils.formatAddress(address);

                SimulatorNode simulator = SimulatorRegistry.getInstance().getSimulator(nodeAddress);
                if (simulator == null) {
                    LOGGER.warn("No simulator found for nodeAddress: {}", nodeAddress);
                }
                else {
                    simulator.processRequest(bidibMessage);
                }
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
        Set<MessageListener> messageListeners, Set<TransferListener> transferListeners, final Context context)
        throws PortNotFoundException, PortNotOpenedException {

        LOGGER.info("Open the serial simulation.");
        setConnectionListener(connectionListener);
        registerListeners(nodeListeners, messageListeners, transferListeners);

        if (context != null) {
            Boolean ignoreWrongMessageNumber =
                context.get("ignoreWrongReceiveMessageNumber", Boolean.class, Boolean.FALSE);
            getSimulationMessageReceiver().setIgnoreWrongMessageNumber(ignoreWrongMessageNumber);
        }

    }

    @Override
    public boolean isOpened() {
        return isOpened;
    }

    private boolean isOpened;

    @Override
    public void close() {
        LOGGER.info("Close the serial simulation.");
        isOpened = false;

        if (getConnectionListener() != null) {
            getConnectionListener().closed(requestedPortName);
        }

        requestedPortName = null;

        // if (simulationInterface != null) {
        // simulationInterface.stop();
        // }

        // release the simulation message receiver
        simulationMessageReceiver = null;

        // remove all simulators from the simulation registry
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
