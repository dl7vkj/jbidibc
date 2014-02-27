package org.bidib.jbidibc;

import java.util.List;

import org.bidib.jbidibc.exception.PortNotFoundException;
import org.bidib.jbidibc.exception.PortNotOpenedException;
import org.bidib.jbidibc.node.AccessoryNode;
import org.bidib.jbidibc.node.BidibNode;
import org.bidib.jbidibc.node.BoosterNode;
import org.bidib.jbidibc.node.CommandStationNode;
import org.bidib.jbidibc.node.RootNode;

public interface BidibInterface {

    static final int DEFAULT_TIMEOUT = /*1500*/200;

    final int responseTimeout = DEFAULT_TIMEOUT;

    /**
     * returns the cached node or creates a new instance
     * 
     * @param node
     *            the node
     * @return the BidibNode instance
     */
    BidibNode getNode(Node node);

    /**
     * @return returns the root node
     */
    RootNode getRootNode();

    /**
     * Returns the provided node as AccessoryNode instance of null if the node is not an 
     * AccessoryNode.
     * @param node the node
     * @return the AccessoryNode instance
     */
    AccessoryNode getAccessoryNode(Node node);

    /**
     * Returns the provided node as BoosterNode instance of null if the node is not an 
     * BoosterNode.
     * @param node the node
     * @return the BoosterNode instance
     */
    BoosterNode getBoosterNode(Node node);

    /**
     * Returns the provided node as CommandStationNode instance of null if the node is not a 
     * CommandStationNode.
     * @param node the node
     * @return the CommandStationNode instance
     */
    CommandStationNode getCommandStationNode(Node node);

    /**
     * Send the bytes to the outputstream.
     * @param bytes the bytes to send
     */
    void send(final byte[] bytes);

    /**
     * Open the communication port with the specified name.
     * @param portName the port name
     * @param connectionListener the connection listener
     * @throws PortNotFoundException thrown if the port is not found in the system
     * @throws PortNotOpenedException thrown if open the communication port failed
     */
    void open(String portName, ConnectionListener connectionListener) throws PortNotFoundException,
        PortNotOpenedException;

    /**
     * @return returns if the port is opened
     */
    boolean isOpened();

    /**
     * Close the communication port.
     */
    void close();

    /**
     * Set the recieve timeout for the port.
     * @param timeout the receive timeout to set
     */
    @Deprecated
    void setReceiveTimeout(int timeout);

    /**
     * Set the logfile for offline research purposes.
     * @param logFile the logfile path
     */
    void setLogFile(String logFile);

    /**
     * @return returns the message receiver
     */
    MessageReceiver getMessageReceiver();

    /**
     * @return returns the list of serial port identifiers that are available in the system.
     */
    List<String> getPortIdentifiers();

    /**
     * Set the ignore wait timeout flag that is used for new nodes.
     * @param ignoreWaitTimeout the ignore wait timeout flag
     */
    void setIgnoreWaitTimeout(boolean ignoreWaitTimeout);

    /**
     * Get the response timeout that is used to wait for response from the nodes.
     * @return the timeout in milliseconds
     */
    int getResponseTimeout();

    /**
     * Set the response timeout that is used to wait for response from the nodes.
     * @param responseTimeout the timeout in milliseconds
     */
    void setResponseTimeout(int responseTimeout);
}
