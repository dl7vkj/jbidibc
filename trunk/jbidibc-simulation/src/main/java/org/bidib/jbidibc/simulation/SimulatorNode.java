package org.bidib.jbidibc.simulation;

import org.bidib.jbidibc.core.Feature;
import org.bidib.jbidibc.core.message.BidibCommand;
import org.bidib.jbidibc.simulation.nodes.FeaturesType;

public interface SimulatorNode {

    /**
     * Start the simulator.
     */
    void start();

    /**
     * Stop the simulator.
     */
    void stop();

    /**
     * Process the bidibMessage.
     * 
     * @param bidibMessage
     *            the bidibMessage to process
     */
    void processRequest(final BidibCommand bidibCommand);

    /**
     * @return the class name of the simulation panel
     */
    String getSimulationPanelClass();

    /**
     * Trigger query the status of the provided port class.
     * 
     * @param portClass
     *            the port class to query the status
     */
    void queryStatus(Class<?> portClass);

    /**
     * @return the uniqueId of the simulated node
     */
    long getUniqueId();

    /**
     * @return the node address
     */
    String getAddress();

    /**
     * Get the local address of the node. This is the address of the node in the subnet.
     * 
     * @return the local address of the node
     */
    String getLocalAddress();

    /**
     * @param featureNum
     *            the requested feature number
     * @return the corresponding feature
     */
    Feature getFeature(int featureNum);

    /**
     * Set the features.
     * 
     * @param featuresType
     *            the features
     */
    void setFeatures(FeaturesType featuresType);

    /**
     * @param nodeName
     *            the node name to set
     */
    void setNodeName(String nodeName);

    /**
     * @param productName
     *            the product name to set
     */
    void setProductName(String productName);

    /**
     * @param protocolVersion
     *            the protocol version to set
     */
    void setProtocolVersion(String protocolVersion);
}
