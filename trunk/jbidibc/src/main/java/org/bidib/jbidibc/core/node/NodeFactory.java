package org.bidib.jbidibc.core.node;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bidib.jbidibc.core.BidibInterface;
import org.bidib.jbidibc.core.MessageReceiver;
import org.bidib.jbidibc.core.Node;
import org.bidib.jbidibc.core.exception.InvalidConfigurationException;
import org.bidib.jbidibc.core.message.RequestFactory;
import org.bidib.jbidibc.core.node.listener.TransferListener;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.bidib.jbidibc.core.utils.NodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeFactory.class);

    private static final int ROOT_ADDRESS = 0;

    private final Map<Integer, BidibNode> nodes = Collections.synchronizedMap(new HashMap<Integer, BidibNode>());

    private MessageReceiver messageReceiver;

    private BidibInterface bidib;

    private RequestFactory requestFactory;

    private boolean ignoreWaitTimeout;

    public NodeFactory() {
    }

    /**
     * @return the bidib
     */
    public BidibInterface getBidib() {
        return bidib;
    }

    /**
     * @param bidib
     *            the bidib to set
     */
    public void setBidib(BidibInterface bidib) {
        this.bidib = bidib;
    }

    /**
     * @return the requestFactory
     */
    public RequestFactory getRequestFactory() {
        return requestFactory;
    }

    /**
     * @param requestFactory
     *            the requestFactory to set
     */
    public void setRequestFactory(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    /**
     * @param ignoreWaitTimeout
     *            the ignoreWaitTimeout flag to set
     */
    public void setIgnoreWaitTimeout(boolean ignoreWaitTimeout) {
        this.ignoreWaitTimeout = ignoreWaitTimeout;
    }

    /**
     * @param messageReceiver
     *            the message receiver to set
     */
    public void setMessageReceiver(MessageReceiver messageReceiver) {
        LOGGER.debug("Set the message receiver: {}", messageReceiver);
        this.messageReceiver = messageReceiver;
    }

    /**
     * Returns the provided node as AccessoryNode instance of null if the node is not an AccessoryNode.
     * 
     * @param node
     *            the node
     * @return the AccessoryNode instance
     */
    public AccessoryNode getAccessoryNode(Node node) {
        BidibNode result = getNode(node);

        if (result instanceof AccessoryNode) {
            return (AccessoryNode) result;
        }
        LOGGER.debug("The requested node is not an AccessoryNode.");
        return null;
    }

    /**
     * Returns the provided node as BoosterNode instance of null if the node is not an BoosterNode.
     * 
     * @param node
     *            the node
     * @return the BoosterNode instance
     */
    public BoosterNode getBoosterNode(Node node) {
        BidibNode bidibNode = getNode(node);

        if (NodeUtils.hasBoosterFunctions(node.getUniqueId())) {
            BoosterNode boosterNode = new BoosterNode(bidibNode);
            LOGGER.debug("prepared booster node: {}", boosterNode);
            return boosterNode;
        }

        LOGGER.debug("The requested node is not a BoosterNode.");
        throw new InvalidConfigurationException("The requested node is not a BoosterNode.");
    }

    /**
     * Returns the provided node as CommandStationNode instance of null if the node is not an CommandStationNode.
     * 
     * @param node
     *            the node
     * @return the CommandStationNode instance
     */
    public CommandStationNode getCommandStationNode(Node node) {
        BidibNode bidibNode = getNode(node);

        if (NodeUtils.hasCommandStationFunctions(node.getUniqueId())) {
            CommandStationNode commandStationNode = new CommandStationNode(bidibNode);
            LOGGER.debug("prepared command station node: {}", commandStationNode);
            return commandStationNode;
        }

        LOGGER.debug("The requested node is not a CommandStationNode.");
        throw new InvalidConfigurationException("The requested node is not a CommandStationNode.");
    }

    public BidibNode createNode(Node node) {
        LOGGER.info("Create the new bidibNode of node: {}", node);
        BidibNode bidibNode = null;

        // check if the node is already in the system
        bidibNode = findNode(node.getAddr());
        if (bidibNode != null) {
            LOGGER.warn("The new node is already registered in the system: {}", bidibNode);
            messageReceiver.removeOrphanNode(node);
            // throw new NodeAlreadyRegisteredException("The new node is already registered in the system: " +
            // bidibNode);
            LOGGER.warn("Removed orphan node.");
        }

        // removeNode(node);
        bidibNode = getNode(node);
        LOGGER.info("createNode returns new bidibNode: {}", bidibNode);
        return bidibNode;
    }

    /**
     * Find a node by it's address
     * 
     * @param address
     *            the node address
     * @return the bidib node
     */
    public BidibNode findNode(byte[] address) {
        LOGGER.debug("Find the bidibNode with address: {}", address);

        BidibNode bidibNode = null;
        synchronized (nodes) {
            int nodeAddress = NodeUtils.convertAddress(address);
            LOGGER.debug("Fetch bidibNode from nodes, nodeAddress: {}", nodeAddress);
            bidibNode = nodes.get(nodeAddress);
            LOGGER.debug("Fetched bidibNode from nodes: {}", bidibNode);
        }
        return bidibNode;
    }

    /**
     * Get a bidib node from the registered nodes or create a new bidib node.
     * 
     * @param node
     *            the node
     * @return the bidib node
     */
    public BidibNode getNode(Node node) {
        LOGGER.debug("Get the bidibNode of node: {}", node);

        BidibNode bidibNode = null;
        synchronized (nodes) {
            int address = NodeUtils.convertAddress(node.getAddr());
            LOGGER.debug("Fetch bidibNode from nodes, address: {}", address);
            bidibNode = nodes.get(address);

            LOGGER.debug("Get the bidibNode from nodesSet with address: {}, bidibNode: {}", address, bidibNode);

            if (bidibNode == null) {
                // get the classId of the new node
                // int classId = ByteUtils.convertLongToUniqueId(node.getUniqueId())[0];
                int classId = ByteUtils.getClassIdFromUniqueId(node.getUniqueId());
                LOGGER.info("Create new bidibNode with classId: {}", classId);

                // create the new node based on the class id
                if ((classId & 0x01) == 1) {
                    bidibNode = new AccessoryNode(node.getAddr(), messageReceiver, ignoreWaitTimeout);
                }
                else {
                    bidibNode = new BidibNode(node.getAddr(), messageReceiver, ignoreWaitTimeout);
                }
                // initialize the node
                bidibNode.setBidib(bidib);
                bidibNode.setRequestFactory(requestFactory);
                bidibNode.setResponseTimeout(bidib.getResponseTimeout());

                // verify that a transfer listener is available
                List<TransferListener> transferListeners = getRootNode().getTransferListeners();
                if (transferListeners != null && transferListeners.size() > 0) {
                    // add the transfer listener
                    bidibNode.addTransferListener(transferListeners.get(0));
                }
                else {
                    LOGGER.error("No transfer listener available on root node!!!!");
                    throw new RuntimeException("No transfer listener available on root node!!!!");
                }

                LOGGER.info("Created new bidibNode: {}, address: {}", bidibNode, address);

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
            // get the node from the registered nodes
            rootNode = (RootNode) nodes.get(ROOT_ADDRESS);

            if (rootNode == null) {
                // no root node registered, create and initialize the root node.
                // the root node has always the local address 0 and is the interface node.
                LOGGER.debug("Create the root node.");
                rootNode = new RootNode(messageReceiver, ignoreWaitTimeout);
                // initialize the root node
                rootNode.setBidib(bidib);
                rootNode.setRequestFactory(requestFactory);
                rootNode.setResponseTimeout(bidib.getResponseTimeout());

                nodes.put(ROOT_ADDRESS, rootNode);
            }
            LOGGER.debug("Root node: {}", rootNode);
        }
        return rootNode;
    }

    public void removeNode(Node node) {
        synchronized (nodes) {
            LOGGER.info("Remove node from bidib nodes: {}", node);

            // if this is a hub node we must remove the children, too ...
            List<Integer> nodesToRemove = new LinkedList<Integer>();
            int address = NodeUtils.convertAddress(node.getAddr());
            nodesToRemove.add(address);

            if (NodeUtils.hasSubNodesFunctions(node.getUniqueId())) {
                byte[] addr = node.getAddr();
                LOGGER
                    .info(
                        "The removed node has subnode functions. We must remove all subnodes, too. Address of current node: {}",
                        addr);
                if (addr != null && addr.length > 0) {
                    for (BidibNode currentNode : nodes.values()) {
                        LOGGER.debug("Check if we must remove the current node: {}", currentNode);
                        byte[] currentAddr = currentNode.getAddr();
                        if (currentAddr.length > addr.length) {
                            // potential subnode
                            if (currentAddr[addr.length - 1] == addr[addr.length - 1]) {
                                // this is a subnode
                                address = NodeUtils.convertAddress(currentAddr);
                                LOGGER.debug("Found a subnode to be removed: {}, address: {}", currentNode, address);

                                nodesToRemove.add(address);
                            }
                        }
                    }
                }
            }

            for (Integer addressKey : nodesToRemove) {
                BidibNode bidibNode = nodes.remove(addressKey);
                if (bidibNode != null) {
                    LOGGER.debug("Removed node that must be removed: {}", bidibNode);
                }
                else {
                    LOGGER.warn("Remove node from nodes map failed, address: {}", addressKey);
                }
            }
        }
    }

    /**
     * Remove all stored nodes but keep the root node!
     */
    public void reset() {
        LOGGER.info("Reset the node factory.");
        synchronized (nodes) {
            LOGGER.debug("Remove all nodes but keep the root node.");
            RootNode rootNode = (RootNode) nodes.get(ROOT_ADDRESS);
            nodes.clear();
            if (rootNode != null) {
                // add the root node again
                nodes.put(ROOT_ADDRESS, rootNode);
            }
        }
    }
}
