package org.bidib.jbidibc.simulation;

import org.bidib.jbidibc.Feature;
import org.bidib.jbidibc.message.BidibCommand;
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
     * @param featureNum
     *            the requested feature number
     * @return the corresponding feature
     */
    Feature getFeature(int featureNum);

    void setFeatures(FeaturesType featuresType);
}
