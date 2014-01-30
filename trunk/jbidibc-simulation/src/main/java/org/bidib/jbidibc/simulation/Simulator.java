package org.bidib.jbidibc.simulation;

import org.bidib.jbidibc.message.BidibMessage;

public interface Simulator {

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
     * @param bidibMessage the bidibMessage to process
     */
    void processRequest(final BidibMessage bidibMessage);

    /**
     * @return the class name of the simulation panel
     */
    String getSimulationPanelClass();

    /**
     * Trigger query the status of the provided port class. 
     * @param portClass the port class to query the status
     */
    void queryStatus(Class<?> portClass);
}
