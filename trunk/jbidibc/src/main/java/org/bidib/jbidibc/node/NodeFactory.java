package org.bidib.jbidibc.node;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bidib.jbidibc.AddressData;
import org.bidib.jbidibc.MessageListener;
import org.bidib.jbidibc.MessageReceiver;
import org.bidib.jbidibc.Node;
import org.bidib.jbidibc.enumeration.BoosterState;
import org.bidib.jbidibc.enumeration.IdentifyState;
import org.bidib.jbidibc.exception.InvalidConfigurationException;
import org.bidib.jbidibc.node.listener.TransferListener;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeFactory.class);

    private static final int ROOT_ADDRESS = 0;

    private final Map<Integer, BidibNode> nodes = Collections.synchronizedMap(new HashMap<Integer, BidibNode>());

    private MessageReceiver messageReceiver;

    public NodeFactory() {
    }

    /**
     * @param messageReceiver
     *            the message receiver to set
     */
    public void setMessageReceiver(MessageReceiver messageReceiver) {
        LOGGER.debug("Set the message receiver: {}", messageReceiver);
        this.messageReceiver = messageReceiver;

        messageReceiver.addMessageListener(new MessageListener() {
            @Override
            public void address(byte[] address, int detectorNumber, Collection<AddressData> addressData) {
            }

            @Override
            public void boosterCurrent(byte[] address, int current) {
            }

            @Override
            public void boosterState(byte[] address, BoosterState state) {
            }

            @Override
            public void boosterTemperature(byte[] address, int temperature) {
            }

            @Override
            public void boosterVoltage(byte[] address, int voltage) {
            }

            @Override
            public void confidence(byte[] address, int valid, int freeze, int signal) {
            }

            @Override
            public void free(byte[] address, int detectorNumber) {
            }

            @Override
            public void identity(byte[] address, IdentifyState identifyState) {
            }

            @Override
            public void key(byte[] address, int keyNumber, int keyState) {
            }

            @Override
            public void nodeLost(Node node) {
                LOGGER.debug("Node lost, node: {}", node);
                removeNode(node);
            }

            @Override
            public void nodeNew(Node node) {
                LOGGER.debug("new node: {}", node);
                removeNode(node);
            }

            @Override
            public void occupied(byte[] address, int detectorNumber) {
            }

            @Override
            public void speed(byte[] address, AddressData addressData, int speed) {
            }
        });
    }

    /**
     * Convert a node address into an integer value.
     * 
     * @param address
     *            node address
     * 
     * @return integer value for that address
     */
    public static int convert(byte[] address) {
        int result = 0;

        if (address != null) {
            for (int index = 0; index < address.length; index++) {
                result += address[index] << index;
            }
        }
        return result;
    }

    public AccessoryNode getAccessoryNode(Node node) {
        BidibNode result = getNode(node);

        if (result instanceof AccessoryNode) {
            return (AccessoryNode) result;
        }
        LOGGER.debug("The requested node is not an AccessoryNode.");
        return null;
    }

    public CommandStationNode getCommandStationNode(Node node) {
        BidibNode result = getNode(node);

        if (node.hasCommandStationFunctions()) {
            CommandStationNode commandStationNode = new CommandStationNode(result);
            LOGGER.debug("prepared command station node: {}", commandStationNode);
            return commandStationNode;
        }

        LOGGER.debug("The requested node is not an CommandStationNode.");
        throw new InvalidConfigurationException("The requested node is not an CommandStationNode.");
    }

    public BidibNode getNode(Node node) {
        LOGGER.debug("Get the bidibNode of node: {}", node);

        BidibNode bidibNode = null;
        synchronized (nodes) {
            int address = convert(node.getAddr());
            LOGGER.debug("Fetch bidibNode from nodes, address: {}", address);
            bidibNode = nodes.get(address);

            LOGGER.debug("Get the bidibNode from nodesSet with address: {}, bidibNode: {}", address, bidibNode);

            if (bidibNode == null) {
                // TODO during testing I've never reached this statement ...
                int classId = ByteUtils.convertLongToUniqueId(node.getUniqueId())[0];
                LOGGER.debug("Create new bidibNode with classId: {}", classId);

                // create the new node based on the class id
                if ((classId & 0x01) == 1) {
                    bidibNode = new AccessoryNode(node.getAddr(), messageReceiver);
                    // add the transfer listener
                    List<TransferListener> transferListeners = getRootNode().getTransferListeners();

                    if (transferListeners != null && transferListeners.size() > 0) {
                        bidibNode.addTransferListener(transferListeners.get(0));
                    }
                }
                // else if ((classId & 0x16) == 1) {
                // bidibNode = new CommandStationNode(node.getAddr());
                // }
                else {
                    bidibNode = new BidibNode(node.getAddr(), messageReceiver);
                    // add the transfer listener
                    bidibNode.addTransferListener(getRootNode().getTransferListeners().get(0));
                }
                LOGGER.info("Create new bidibNode: {}, address: {}", bidibNode, address);

                nodes.put(address, bidibNode);
            }
        }
        return bidibNode;
    }

    /**
     * Get the root node of the system. This is the node that represents the master. Creates a new instance of root node
     * if no root node is stored.
     * 
     * @return the root node
     */
    public RootNode getRootNode() {
        LOGGER.debug("Get the root node.");
        RootNode rootNode = null;
        synchronized (nodes) {

            rootNode = (RootNode) nodes.get(ROOT_ADDRESS);

            if (rootNode == null) {
                LOGGER.debug("Create the root node.");
                rootNode = new RootNode(messageReceiver);
                nodes.put(ROOT_ADDRESS, rootNode);
            }
            LOGGER.debug("Root node: {}", rootNode);
        }

        return rootNode;
    }

    private void removeNode(Node node) {
        synchronized (nodes) {
            LOGGER.debug("Remove node from nodes: {}", node);
            nodes.remove(convert(node.getAddr()));
        }
    }

    /**
     * Remove all stored nodes.
     */
    public void reset() {
        LOGGER.info("Reset the node factory.");
        synchronized (nodes) {
            LOGGER.debug("Remove all nodes.");
            nodes.clear();
        }
    }
}
