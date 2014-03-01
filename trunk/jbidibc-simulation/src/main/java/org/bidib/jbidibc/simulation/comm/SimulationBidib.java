package org.bidib.jbidibc.simulation.comm;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.bidib.jbidibc.BidibInterface;
import org.bidib.jbidibc.ConnectionListener;
import org.bidib.jbidibc.MessageReceiver;
import org.bidib.jbidibc.core.AbstractBidib;
import org.bidib.jbidibc.core.Context;
import org.bidib.jbidibc.exception.PortNotFoundException;
import org.bidib.jbidibc.exception.PortNotOpenedException;
import org.bidib.jbidibc.message.BidibCommand;
import org.bidib.jbidibc.message.BidibMessage;
import org.bidib.jbidibc.message.RequestFactory;
import org.bidib.jbidibc.node.BidibNode;
import org.bidib.jbidibc.node.NodeFactory;
import org.bidib.jbidibc.node.RootNode;
import org.bidib.jbidibc.simulation.SimulatorNode;
import org.bidib.jbidibc.simulation.SimulatorRegistry;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulationBidib extends AbstractBidib {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationBidib.class);

    private static SimulationBidib INSTANCE;

    private String connectedPortName;

    // private int responseTimeout = BidibInterface.DEFAULT_TIMEOUT * 100;

    private boolean isOpened = false;

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

    @Override
    protected MessageReceiver createMessageReceiver(NodeFactory nodeFactory) {
        return new MessageReceiver(
            nodeFactory) {

            @Override
            public byte[] getRemainingOutputBuffer() {
                return null;
            }
        };
    }

    public static synchronized BidibInterface getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SimulationBidib();
            INSTANCE.initialize();
        }
        return INSTANCE;
    }

    @Override
    public RootNode getRootNode() {
        RootNode rootNode = super.getRootNode();

        String nodeAddress = ByteUtils.bytesToHex(rootNode.getAddr());
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
            // TODO change the call to request factory
            List<BidibCommand> bidibMessages = new RequestFactory().create(bytes);
            for (BidibCommand bidibMessage : bidibMessages) {
                byte[] address = ((BidibMessage) bidibMessage).getAddr();

                BidibNode bidibNode = getNodeFactory().findNode(address);
                String nodeAddress = ByteUtils.bytesToHex(bidibNode.getAddr());

                SimulatorNode simulator = SimulatorRegistry.getInstance().getSimulator(nodeAddress);
                if (simulator == null) {
                    LOGGER.warn("No simulator found for nodeAddress: {}", nodeAddress);
                }

                Context context = null;
                simulator.processRequest(context, bidibMessage);

                LOGGER.debug("Forwarded message to simulator: {}", bidibMessage);
            }
        }
        catch (Exception ex) {
            LOGGER.warn("Process request failed.", ex);
        }
    }

    @Override
    public void open(String portName, ConnectionListener connectionListener) throws PortNotFoundException,
        PortNotOpenedException {
        LOGGER.info("Open port: {}", portName);

        setConnectionListener(connectionListener);

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

        if (getConnectionListener() != null) {
            getConnectionListener().closed(connectedPortName);
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
    public List<String> getPortIdentifiers() {
        List<String> portIdentifiers = new LinkedList<>();
        portIdentifiers.add("mock");
        return portIdentifiers;
    }
}
