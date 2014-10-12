package org.bidib.jbidibc.simulation.comm;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;
import java.util.Set;

import org.bidib.jbidibc.BidibInterface;
import org.bidib.jbidibc.ConnectionListener;
import org.bidib.jbidibc.MessageListener;
import org.bidib.jbidibc.Node;
import org.bidib.jbidibc.NodeListener;
import org.bidib.jbidibc.exception.PortNotFoundException;
import org.bidib.jbidibc.exception.PortNotOpenedException;
import org.bidib.jbidibc.helpers.AbstractBidib;
import org.bidib.jbidibc.helpers.BidibMessageProcessor;
import org.bidib.jbidibc.helpers.Context;
import org.bidib.jbidibc.net.NetBidib;
import org.bidib.jbidibc.node.AccessoryNode;
import org.bidib.jbidibc.node.BidibNode;
import org.bidib.jbidibc.node.BoosterNode;
import org.bidib.jbidibc.node.CommandStationNode;
import org.bidib.jbidibc.node.RootNode;
import org.bidib.jbidibc.node.listener.TransferListener;
import org.bidib.jbidibc.simulation.SimulationInterface;
import org.bidib.jbidibc.simulation.SimulatorNode;
import org.bidib.jbidibc.simulation.SimulatorRegistry;
import org.bidib.jbidibc.simulation.net.SimulationNetBidib;
import org.bidib.jbidibc.simulation.serial.SimulationSerialBidib;
import org.bidib.jbidibc.utils.ByteUtils;
import org.bidib.jbidibc.utils.NodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulationBidib implements BidibInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationBidib.class);

    private static SimulationBidib INSTANCE;

    private String connectedPortName;

    private boolean isOpened;

    private boolean ignoreWaitTimeout;

    private int responseTimeout;

    private SimulationInterface delegateBidib;

    // ////////////////////////////////////////////////////////////////////////////
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

    public static synchronized BidibInterface getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SimulationBidib();
            INSTANCE.initialize();
        }
        return INSTANCE;
    }

    /**
     * Initialize the instance. This must only be called from this class
     */
    protected void initialize() {
        LOGGER.info("Initialize SimulationBidib.");
    }

    @Override
    public RootNode getRootNode() {
        RootNode rootNode = null;
        try {
            rootNode = delegateBidib.getRootNode();
        }
        catch (Exception ex) {
            LOGGER.warn("Get the root node failed.", ex);
        }

        String nodeAddress = NodeUtils.formatAddress(rootNode.getAddr());
        SimulatorNode simulator = SimulatorRegistry.getInstance().getSimulator(nodeAddress);
        if (simulator == null) {
            LOGGER.warn("No simulator configured for the root node.");
        }

        return rootNode;
    }

    @Override
    public void send(byte[] bytes) {
        LOGGER.info("Send is called with bytes: {}", ByteUtils.bytesToHex(bytes));

        try {
            delegateBidib.send(bytes);
        }
        catch (Exception ex) {
            LOGGER.warn("Send bytes to delegate failed.", ex);
        }
    }

    @Override
    public void open(
        String portName, ConnectionListener connectionListener, Set<NodeListener> nodeListeners,
        Set<MessageListener> messageListeners, Set<TransferListener> transferListeners, final Context context)
        throws PortNotFoundException, PortNotOpenedException {

        LOGGER.info("Open port: {}", portName);

        // select the simulation interface
        if ("udp".equals(portName)) {
            // TODO change the fix portName
            portName = "localhost:" + NetBidib.BIDIB_UDP_PORT_NUMBER;
            delegateBidib = new SimulationNetBidib();
        }
        else {
            delegateBidib = new SimulationSerialBidib();
        }

        // make the initialize method accessible and call it ...
        try {
            Method method = AbstractBidib.class.getDeclaredMethod("initialize", null);
            method.setAccessible(true);
            method.invoke(delegateBidib, null);
        }
        catch (Exception ex) {
            LOGGER.warn("Call initialize on delegateBidib failed.", ex);
        }

        delegateBidib.setIgnoreWaitTimeout(ignoreWaitTimeout);
        delegateBidib.setResponseTimeout(responseTimeout);
        delegateBidib.setConnectionListener(connectionListener);
        delegateBidib.registerListeners(nodeListeners, messageListeners, transferListeners);
        // start the simulation part
        // TODO make the path to the simulation configuration configurable
        LOGGER.info("Current working dir: {}", System.getProperty("user.dir"));
        String simulationConfig = "/simulation/simulation.xml";
        try {
            URL url = getClass().getResource("/data/simulation/simulation.xml");
            if (url != null) {
                simulationConfig = "/data/simulation/simulation.xml";
                LOGGER.info("Use simulation configuration from: {}", simulationConfig);
            }
            else {
                LOGGER.info("No simulation configuration found under /data/simulation.");
            }
        }
        catch (Exception ex) {
            LOGGER.warn("Load simulation configuration from data/simulation failed.", ex);
        }
        delegateBidib.start(simulationConfig);

        // open the interface
        delegateBidib.open(portName, connectionListener, nodeListeners, messageListeners, transferListeners, context);

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
        try {
            delegateBidib.close();
        }
        catch (Exception ex) {
            LOGGER.warn("Close delegateBidib failed.", ex);
        }
        isOpened = false;
        connectedPortName = null;

        try {
            delegateBidib.stop();
        }
        catch (Exception ex) {
            LOGGER.warn("Stop delegateBidib failed.", ex);
        }
    }

    @Override
    public List<String> getPortIdentifiers() {
        return delegateBidib.getPortIdentifiers();
    }

    @Override
    public BidibNode getNode(Node node) {
        return delegateBidib.getNode(node);
    }

    @Override
    public AccessoryNode getAccessoryNode(Node node) {
        return delegateBidib.getAccessoryNode(node);
    }

    @Override
    public BoosterNode getBoosterNode(Node node) {
        return delegateBidib.getBoosterNode(node);
    }

    @Override
    public CommandStationNode getCommandStationNode(Node node) {
        return delegateBidib.getCommandStationNode(node);
    }

    @Override
    public BidibMessageProcessor getMessageReceiver() {
        LOGGER.info("getMessageReceiver returns the message receiver of the delegate!");
        return delegateBidib.getMessageReceiver();
    }

    @Override
    public void setIgnoreWaitTimeout(boolean ignoreWaitTimeout) {
        this.ignoreWaitTimeout = ignoreWaitTimeout;
        if (delegateBidib != null) {
            delegateBidib.setIgnoreWaitTimeout(ignoreWaitTimeout);
        }
    }

    @Override
    public void setResponseTimeout(int timeout) {
        LOGGER.info("Set the response timeout for simulation to: {}", timeout * 1000);
        this.responseTimeout = timeout * 1000;
        if (delegateBidib != null) {
            delegateBidib.setResponseTimeout(responseTimeout);
        }
    }

    @Override
    public int getResponseTimeout() {
        return delegateBidib.getResponseTimeout();
    }

    @Override
    public void setLogFile(String logFile) {
        delegateBidib.setLogFile(logFile);
    }
}
