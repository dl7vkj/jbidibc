package org.bidib.jbidibc.simulation.net;

import org.bidib.jbidibc.BidibInterface;
import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.ConnectionListener;
import org.bidib.jbidibc.exception.PortNotFoundException;
import org.bidib.jbidibc.exception.PortNotOpenedException;
import org.bidib.jbidibc.net.NetBidib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class NetBidibTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetBidibTest.class);

    private SimulationNetBidib simulationNetBidib;

    @BeforeClass
    public void prepare() {
        LOGGER.info("Prepare the simulator.");

        // create the test instance
        simulationNetBidib = new SimulationTestNetBidib();
        simulationNetBidib.start();

        LOGGER.info("Prepared and started the simulator.");
    }

    @AfterClass
    public void destroy() {
        LOGGER.info("Stop the simulation.");

        if (simulationNetBidib != null) {
            simulationNetBidib.stop();
        }

        LOGGER.info("Stopped the simulation.");
    }

    @Test
    public void open() throws PortNotFoundException, PortNotOpenedException {

        NetBidib netBidib = (NetBidib) NetBidib.getInstance();
        Assert.assertNotNull(netBidib);

        netBidib.setResponseTimeout(BidibInterface.DEFAULT_TIMEOUT * 100);

        netBidib.open("localhost:" + NetBidib.BIDIB_UDP_PORT_NUMBER, new ConnectionListener() {

            @Override
            public void opened(String port) {
                // TODO Auto-generated method stub
                LOGGER.info("The port was opened: {}", port);
            }

            @Override
            public void closed(String port) {
                // TODO Auto-generated method stub
                LOGGER.info("The port was closed: {}", port);
            }
        });

        Assert.assertNotNull(netBidib.getRootNode());
        Assert.assertEquals(netBidib.getRootNode().getNodeMagic(), Integer.valueOf(BidibLibrary.BIDIB_SYS_MAGIC));

        netBidib.close();
    }
}
