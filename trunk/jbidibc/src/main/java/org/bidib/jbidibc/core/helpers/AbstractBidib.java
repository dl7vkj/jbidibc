package org.bidib.jbidibc.core.helpers;

import java.util.Set;

import org.bidib.jbidibc.core.BidibInterface;
import org.bidib.jbidibc.core.ConnectionListener;
import org.bidib.jbidibc.core.MessageListener;
import org.bidib.jbidibc.core.Node;
import org.bidib.jbidibc.core.NodeListener;
import org.bidib.jbidibc.core.message.RequestFactory;
import org.bidib.jbidibc.core.node.AccessoryNode;
import org.bidib.jbidibc.core.node.BidibNode;
import org.bidib.jbidibc.core.node.BoosterNode;
import org.bidib.jbidibc.core.node.CommandStationNode;
import org.bidib.jbidibc.core.node.NodeFactory;
import org.bidib.jbidibc.core.node.RootNode;
import org.bidib.jbidibc.core.node.listener.TransferListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBidib implements BidibInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBidib.class);

    private int responseTimeout = BidibInterface.DEFAULT_TIMEOUT;

    public static final int DEFAULT_TIMEOUT = /* 1500 */200;

    private BidibMessageProcessor messageReceiver;

    private NodeFactory nodeFactory;

    private RequestFactory requestFactory;

    private ConnectionListener connectionListener;

    protected AbstractBidib() {
    }

    /**
     * Initialize the instance. This must only be called from this class
     */
    protected void initialize() {
        LOGGER.info("Initialize AbstractBidib, create NodeFactory.");
        nodeFactory = new NodeFactory();
        LOGGER.info("Created nodeFactory: {}", nodeFactory);
        nodeFactory.setBidib(this);

        // create the request factory
        requestFactory = new RequestFactory();
        nodeFactory.setRequestFactory(requestFactory);

        // create the message receiver
        messageReceiver = createMessageReceiver(nodeFactory);
    }

    /**
     * Create the message receiver that processes the messages that are received from the <b>interface</b>.
     * 
     * @param nodeFactory
     *            the node factory
     * @return the message receiver
     */
    protected abstract BidibMessageProcessor createMessageReceiver(NodeFactory nodeFactory);

    /**
     * Register the node and message listeners.
     * 
     * @param nodeListeners
     *            the node listeners
     * @param messageListeners
     *            the message listeners
     */
    public void registerListeners(
        Set<NodeListener> nodeListeners, Set<MessageListener> messageListeners, Set<TransferListener> transferListeners) {

        for (NodeListener nodeListener : nodeListeners) {
            messageReceiver.addNodeListener(nodeListener);
        }
        for (MessageListener messageListener : messageListeners) {
            messageReceiver.addMessageListener(messageListener);
        }

        LOGGER.info("Add the transfer listeners to the root node.");
        for (TransferListener transferListener : transferListeners) {
            getRootNode().addTransferListener(transferListener);
        }

    }

    @Override
    public BidibMessageProcessor getMessageReceiver() {
        return messageReceiver;
    }

    /**
     * @return the connectionListener
     */
    public ConnectionListener getConnectionListener() {
        return connectionListener;
    }

    /**
     * @param connectionListener
     *            the connectionListener to set
     */
    public void setConnectionListener(ConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }

    protected NodeFactory getNodeFactory() {
        return nodeFactory;
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
    public BidibNode getNode(Node node) {
        return nodeFactory.getNode(node);
    }

    @Override
    public RootNode getRootNode() {
        return nodeFactory.getRootNode();
    }

    @Override
    public void setIgnoreWaitTimeout(boolean ignoreWaitTimeout) {
        if (nodeFactory != null) {
            LOGGER.info("Set ignoreWaitTimeout flag: {}", ignoreWaitTimeout);
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
        LOGGER.info("Set the response timeout: {}", responseTimeout);
        this.responseTimeout = responseTimeout;
    }
}
