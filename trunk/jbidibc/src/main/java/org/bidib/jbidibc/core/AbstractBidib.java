package org.bidib.jbidibc.core;

import org.bidib.jbidibc.BidibInterface;
import org.bidib.jbidibc.ConnectionListener;
import org.bidib.jbidibc.MessageReceiver;
import org.bidib.jbidibc.Node;
import org.bidib.jbidibc.message.RequestFactory;
import org.bidib.jbidibc.node.AccessoryNode;
import org.bidib.jbidibc.node.BidibNode;
import org.bidib.jbidibc.node.BoosterNode;
import org.bidib.jbidibc.node.CommandStationNode;
import org.bidib.jbidibc.node.NodeFactory;
import org.bidib.jbidibc.node.RootNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBidib implements BidibInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBidib.class);

    private int responseTimeout = BidibInterface.DEFAULT_TIMEOUT;

    public static final int DEFAULT_TIMEOUT = /* 1500 */200;

    private MessageReceiver messageReceiver;

    private NodeFactory nodeFactory;

    private RequestFactory requestFactory;

    private ConnectionListener connectionListener;

    protected AbstractBidib() {
    }

    /**
     * Initialize the instance. This must only be called from this class
     */
    protected void initialize() {
        LOGGER.info("Initialize AbstractBidib.");
        nodeFactory = new NodeFactory();
        nodeFactory.setBidib(this);
        requestFactory = new RequestFactory();
        nodeFactory.setRequestFactory(requestFactory);

        // create the message receiver
        messageReceiver = createMessageReceiver(nodeFactory);
    }

    protected abstract MessageReceiver createMessageReceiver(NodeFactory nodeFactory);

    @Override
    public MessageReceiver getMessageReceiver() {
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
