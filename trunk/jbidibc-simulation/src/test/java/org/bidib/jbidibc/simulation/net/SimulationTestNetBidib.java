package org.bidib.jbidibc.simulation.net;

import org.bidib.jbidibc.net.NetMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulationTestNetBidib extends SimulationNetBidib {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationTestNetBidib.class);

    public SimulationTestNetBidib() {
        LOGGER.info("Create new instance of SimulationTestNetBidib.");
    }

    @Override
    protected NetMessageHandler createMessageHandler() {
        LOGGER.info("Create the SimulationNetMessageReceiver.");
        return new SimulationNetMessageHandler(getSimulatorNode());
    }

    public static void main(String[] args) {
        new SimulationNetBidib().start();
    }
}
