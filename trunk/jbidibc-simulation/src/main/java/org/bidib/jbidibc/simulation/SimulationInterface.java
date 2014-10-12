package org.bidib.jbidibc.simulation;

import java.util.Set;

import org.bidib.jbidibc.core.BidibInterface;
import org.bidib.jbidibc.core.ConnectionListener;
import org.bidib.jbidibc.core.MessageListener;
import org.bidib.jbidibc.core.NodeListener;
import org.bidib.jbidibc.core.exception.PortNotFoundException;
import org.bidib.jbidibc.core.exception.PortNotOpenedException;
import org.bidib.jbidibc.core.helpers.Context;
import org.bidib.jbidibc.core.node.listener.TransferListener;

public interface SimulationInterface extends BidibInterface {

    void start(String simulationConfigurationLocation);

    void stop();

    void open(
        String portName, ConnectionListener connectionListener, Set<NodeListener> nodeListeners,
        Set<MessageListener> messageListeners, Set<TransferListener> transferListeners, final Context context)
        throws PortNotFoundException, PortNotOpenedException;

    /**
     * Register the node and message listeners.
     * 
     * @param nodeListeners
     *            the node listeners
     * @param messageListeners
     *            the message listeners
     */
    void registerListeners(
        Set<NodeListener> nodeListeners, Set<MessageListener> messageListeners, Set<TransferListener> transferListeners);

    /**
     * @param connectionListener
     *            the connectionListener to set
     */
    void setConnectionListener(ConnectionListener connectionListener);

    void send(byte[] bytes);
}
