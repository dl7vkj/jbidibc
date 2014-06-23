package org.bidib.jbidibc.simulation;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.bidib.jbidibc.simulation.nodes.BacklightPortParamsType;
import org.bidib.jbidibc.simulation.nodes.LightPortParamsType;
import org.bidib.jbidibc.simulation.nodes.MasterType;
import org.bidib.jbidibc.simulation.nodes.NodeType;
import org.bidib.jbidibc.simulation.nodes.Simulation;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

public class SimulationConfigTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationConfigTest.class);

    private static final String JAXB_PACKAGE = "org.bidib.jbidibc.simulation.nodes";

    private static final String XSD_LOCATION = "/xsd/simulation.xsd";

    @Test
    public void loadNodesTest() throws FileNotFoundException, JAXBException, SAXException {

        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

        // create a validating unmarshaller
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        StreamSource streamSource = new StreamSource(SimulationConfigTest.class.getResourceAsStream(XSD_LOCATION));
        Schema schema = schemaFactory.newSchema(streamSource);
        unmarshaller.setSchema(schema);

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
        LOGGER.info("expected: {}, received: {}", ByteUtils.bytesToHex(expected),
            ByteUtils.bytesToHex(master.getUniqueId()));

        Assert.assertEquals(master.getUniqueId(), expected);
        Assert.assertEquals(ByteUtils.convertUniqueIdToLong(master.getUniqueId()), 0xd2000d680064eaL);

        Assert.assertNotNull(master.getSubNodes());
        Assert.assertNotNull(master.getSubNodes().getNode());
        List<NodeType> nodes = master.getSubNodes().getNode();
        Assert.assertEquals(nodes.size(), 2);
        Assert.assertEquals(ByteUtils.convertUniqueIdToLong(nodes.get(0).getUniqueId()), 0x05340d6B901234L);
        Assert.assertEquals(ByteUtils.convertUniqueIdToLong(nodes.get(1).getUniqueId()), 0x05343e97901235L);

        // first port has LPORTs and SPORTs configured
        NodeType node = nodes.get(0);
        Assert.assertNotNull(node.getLPORT());
        Assert.assertEquals(node.getLPORT().getCount().intValue(), 32);
        Assert.assertNotNull(node.getSPORT());
        Assert.assertEquals(node.getSPORT().getCount().intValue(), 8);

        // second port has no LPORTs but SPORTs configured
        node = nodes.get(1);
        Assert.assertNull(node.getLPORT());
        Assert.assertNotNull(node.getSPORT());
        Assert.assertEquals(node.getSPORT().getCount().intValue(), 8);
    }

    @Test
    public void loadNodesOneDMXTest() throws FileNotFoundException, JAXBException, SAXException {

        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

        // create a validating unmarshaller
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        StreamSource streamSource = new StreamSource(SimulationConfigTest.class.getResourceAsStream(XSD_LOCATION));
        Schema schema = schemaFactory.newSchema(streamSource);
        unmarshaller.setSchema(schema);

        Reporter.log("Load the simulation file.", true);
        InputStream is = SimulationConfigTest.class.getResourceAsStream("/nodes/simulation-OneDMX.xml");
        Assert.assertNotNull(is);

        LOGGER.info("Opened stream: {}", is);
        Simulation simulation = (Simulation) unmarshaller.unmarshal(is);
        Assert.assertNotNull(simulation);

        MasterType master = simulation.getMaster();
        Assert.assertNotNull(master);

        Assert.assertNotNull(master.getUniqueId());

        byte[] expected = ByteUtils.convertLongToUniqueId(0xd2000d680064eaL);
        LOGGER.info("expected: {}, received: {}", ByteUtils.bytesToHex(expected),
            ByteUtils.bytesToHex(master.getUniqueId()));

        Assert.assertEquals(master.getUniqueId(), expected);
        Assert.assertEquals(ByteUtils.convertUniqueIdToLong(master.getUniqueId()), 0xd2000d680064eaL);

        Assert.assertNotNull(master.getSubNodes());
        Assert.assertNotNull(master.getSubNodes().getNode());
        List<NodeType> nodes = master.getSubNodes().getNode();
        Assert.assertEquals(nodes.size(), 1);
        Assert.assertEquals(ByteUtils.convertUniqueIdToLong(nodes.get(0).getUniqueId()), 0x05340D73901235L);

        // OneDMX has LPORTs and BACKLIGHTPORTs configured
        NodeType node = nodes.get(0);
        Assert.assertNotNull(node.getLPORT());
        Assert.assertEquals(node.getLPORT().getCount().intValue(), 128);
        Assert.assertNotNull(node.getBACKLIGHT());
        Assert.assertEquals(node.getBACKLIGHT().getCount().intValue(), 16);

        Assert.assertNotNull(node.getINPUT());
        Assert.assertEquals(node.getINPUT().getCount().intValue(), 8);

        Assert.assertNotNull(node.getLPORT().getPort());
        Assert.assertNotNull(node.getLPORT().getPort().get(0));

        LightPortParamsType lightPortParamsType = node.getLPORT().getPort().get(0);
        Assert.assertEquals(lightPortParamsType.getDmxMapping(), 1);
        Assert.assertEquals(lightPortParamsType.getIntensityOff(), 20);
        Assert.assertEquals(lightPortParamsType.getIntensityOn(), 240);

        lightPortParamsType = node.getLPORT().getPort().get(1);
        Assert.assertEquals(lightPortParamsType.getDmxMapping(), 4);

        BacklightPortParamsType backlightPortParamsType = node.getBACKLIGHT().getPort().get(0);
        Assert.assertEquals(backlightPortParamsType.getDmxMapping(), 1);
        backlightPortParamsType = node.getBACKLIGHT().getPort().get(1);
        Assert.assertEquals(backlightPortParamsType.getDmxMapping(), 3);
    }
}
