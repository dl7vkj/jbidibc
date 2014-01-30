package org.bidib.jbidibc.simulation;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.bidib.jbidibc.simulation.nodes.MasterType;
import org.bidib.jbidibc.simulation.nodes.NodeType;
import org.bidib.jbidibc.simulation.nodes.Simulation;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class SimulationConfigTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationConfigTest.class);

    private static final String JAXB_PACKAGE = "org.bidib.jbidibc.simulation.nodes";

    @Test
    public void loadNodesTest() throws FileNotFoundException, JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        Reporter.log("Load the simulation file.", true);
        InputStream is = SimulationConfigTest.class.getResourceAsStream("/nodes/simulation.xml");
        Assert.assertNotNull(is);

        LOGGER.info("Opened stream: {}", is);
        Simulation simulation = (Simulation) unmarshaller.unmarshal(is);
        Assert.assertNotNull(simulation);

        MasterType master = simulation.getMaster();
        Assert.assertNotNull(master);

        Assert.assertNotNull(master.getUniqueId());

        byte[] expected = ByteUtils.convertLongToUniqueId(0xd2000d680064eaL);
        LOGGER.info("expected: {}, received: {}", ByteUtils.bytesToHex(expected), ByteUtils.bytesToHex(master
            .getUniqueId()));

        Assert.assertEquals(master.getUniqueId(), expected);
        Assert.assertEquals(ByteUtils.convertUniqueIdToLong(master.getUniqueId()), 0xd2000d680064eaL);

        Assert.assertNotNull(master.getSubNodes());
        Assert.assertNotNull(master.getSubNodes().getNode());
        List<NodeType> nodes = master.getSubNodes().getNode();
        Assert.assertEquals(nodes.size(), 2);
        Assert.assertEquals(ByteUtils.convertUniqueIdToLong(nodes.get(0).getUniqueId()), 0x05340d6B901234L);
        Assert.assertEquals(ByteUtils.convertUniqueIdToLong(nodes.get(1).getUniqueId()), 0x05343e97901235L);
    }
}
