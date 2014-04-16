package org.bidib.jbidibc.simulation.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandaloneNetSimulator {
    private static final Logger LOGGER = LoggerFactory.getLogger(StandaloneNetSimulator.class);

    private static final String SIMULATION_CONFIGURATION_LOCATION = "/simulation/simulation-demo.xml";

    public static void main(String[] args) {

        LOGGER.info("Prepare the simulator.");
        SimulationNetBidib simulationNetBidib = null;
        try {
            // create the test instance
            simulationNetBidib = new SimulationNetBidib();
            simulationNetBidib.start(SIMULATION_CONFIGURATION_LOCATION);

            LOGGER.info("Prepared and started the simulator.");
            while (true) {
                Thread.sleep(1000L);
            }

        }
        catch (Exception ex) {
            LOGGER.warn("Start simulator failed.", ex);
        }
        finally {
            if (simulationNetBidib != null) {
                LOGGER.info("Stop the simulator.");
                simulationNetBidib.stop();
            }
        }
        LOGGER.info("Simulator stopped.");
    }
}
