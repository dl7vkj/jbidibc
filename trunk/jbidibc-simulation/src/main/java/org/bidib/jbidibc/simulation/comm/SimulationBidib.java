package org.bidib.jbidibc.simulation.comm;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.bidib.jbidibc.Bidib;
import org.bidib.jbidibc.BidibInterface;
import org.bidib.jbidibc.ConnectionListener;
import org.bidib.jbidibc.MessageReceiver;
import org.bidib.jbidibc.Node;
import org.bidib.jbidibc.exception.PortNotFoundException;
import org.bidib.jbidibc.exception.PortNotOpenedException;
import org.bidib.jbidibc.message.BidibMessage;
import org.bidib.jbidibc.message.RequestFactory;
import org.bidib.jbidibc.node.AccessoryNode;
import org.bidib.jbidibc.node.BidibNode;
import org.bidib.jbidibc.node.BoosterNode;
import org.bidib.jbidibc.node.CommandStationNode;
import org.bidib.jbidibc.node.NodeFactory;
import org.bidib.jbidibc.node.RootNode;
import org.bidib.jbidibc.simulation.SimulatorNode;
import org.bidib.jbidibc.simulation.SimulatorRegistry;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulationBidib implements BidibInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationBidib.class);

    private static SimulationBidib INSTANCE;

    private MessageReceiver messageReceiver;

    private NodeFactory nodeFactory;

    private String connectedPortName;

    private ConnectionListener connectionListener;

    private int responseTimeout = Bidib.DEFAULT_TIMEOUT * 100;

    //////////////////////////////////////////////////////////////////////////////
    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    LOGGER.debug("Close the communication ports and perform cleanup.");
                    getInstance().close();
                }
                catch (Exception e) {
                }
            }
        });
    }

    private SimulationBidib() {
    }

    /**
     * Initialize the instance. This must only be called from this class
     */
    protected void initialize() {
        LOGGER.info("Initialize SimulationBidib.");
        nodeFactory = new NodeFactory();
        nodeFactory.setBidib(this);
        // create the message receiver
        messageReceiver = new MessageReceiver(
            nodeFactory) {

            @Override
            public byte[] getRemainingOutputBuffer() {
                return null;
            }
        };
        //        messageReceiver.setBidib(this);
    }

    public static synchronized BidibInterface getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SimulationBidib();
            INSTANCE.initialize();
        }
        return INSTANCE;
    }

    @Override
    public BidibNode getNode(Node node) {
        LOGGER.info("Get node: {}", node);
        return nodeFactory.getNode(node);
    }

    @Override
    public RootNode getRootNode() {
        RootNode rootNode = nodeFactory.getRootNode();

        String nodeAddress = ByteUtils.bytesToHex(rootNode.getAddr());
        SimulatorNode simulator = SimulatorRegistry.getInstance().getSimulator(nodeAddress);
        if (simulator == null) {
            LOGGER.warn("No simulator configured for the root node.");
        }
        return rootNode;
    }

    @Override
    public AccessoryNode getAccessoryNode(Node node) {
        return nodeFactory.getAccessoryNode(node);
    }

    @Override
    public BoosterNode getBoosterNode(Node node) {
        return nodeFactory.getBoosterNode(node);
    }

    @Override
    public CommandStationNode getCommandStationNode(Node node) {
        return nodeFactory.getCommandStationNode(node);
    }

    @Override
    public void send(byte[] bytes) {
        LOGGER.info("Send is called with bytes: {}", ByteUtils.bytesToHex(bytes));

        try {
            List<BidibMessage> bidibMessages = RequestFactory.create(bytes);
            for (BidibMessage bidibMessage : bidibMessages) {
                byte[] address = bidibMessage.getAddr();

                BidibNode bidibNode = nodeFactory.findNode(address);
                String nodeAddress = ByteUtils.bytesToHex(bidibNode.getAddr());

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

    private boolean isOpened = false;

    @Override
    public void open(String portName, ConnectionListener connectionListener) throws PortNotFoundException,
        PortNotOpenedException {
        LOGGER.info("Open port: {}", portName);

        this.connectionListener = connectionListener;

        // TODO load the SimulatorRegistry with the simulation configuration
        SimulatorRegistry.getInstance().removeAll();

        String path = getClass().getResource("/simulation/simulation.xml").getPath();
        File simulationConfiguration = new File(path);
        SimulatorRegistry.getInstance().loadSimulationConfiguration(simulationConfiguration, this);

        connectedPortName = portName;
        isOpened = true;
    }

    @Override
    public boolean isOpened() {
        return isOpened;
    }

    @Override
    public void close() {
        LOGGER.info("Close port, connectedPortName: {}", connectedPortName);

        if (connectionListener != null) {
            connectionListener.closed(connectedPortName);
        }

        isOpened = false;
        connectedPortName = null;

        // TODO remove the simulation
        SimulatorRegistry.getInstance().removeAll();
    }

    @Override
    public void setReceiveTimeout(int timeout) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setLogFile(String logFile) {
        // TODO Auto-generated method stub

    }

    @Override
    public MessageReceiver getMessageReceiver() {
        return messageReceiver;
    }

    @Override
    public List<String> getPortIdentifiers() {
        List<String> portIdentifiers = new LinkedList<>();
        portIdentifiers.add("mock");
        return portIdentifiers;
    }

    @Override
    public void setIgnoreWaitTimeout(boolean ignoreWaitTimeout) {
        if (nodeFactory != null) {
            nodeFactory.setIgnoreWaitTimeout(ignoreWaitTimeout);
        }
        else {
            LOGGER.warn("The node factory is not available, set the ignoreWaitTimeout is discarded.");
        }
    }

    @Override
    public int getResponseTimeout() {
        return responseTimeout;
    }

    @Override
    public void setResponseTimeout(int responseTimeout) {
        LOGGER.info("Set the response timeout: {} --> used for simulation: {}", responseTimeout, responseTimeout * 100);
        this.responseTimeout = responseTimeout * 100;
    }
}
