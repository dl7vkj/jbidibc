package org.bidib.jbidibc.schema.bidib;

import java.io.InputStream;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.bidib.schema.firmware.DeviceNode;
import org.bidib.schema.firmware.Firmware;
import org.bidib.schema.firmware.FirmwareDefinitionType;
import org.bidib.schema.firmware.FirmwareNode;
import org.bidib.schema.firmware.NodeType;
import org.bidib.schema.firmware.SimpleNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

public class FirmwareFactoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FirmwareFactoryTest.class);

    private static final String JAXB_PACKAGE = "org.bidib.schema.firmware";

    private static final String XSD_LOCATION = "/xsd/firmware.xsd";

    @Test
    public void loadFirmwareDefinition() throws JAXBException, SAXException, XMLStreamException {
        LOGGER.info("Prepare the JAXBContext to load the firmware defintion file.");

        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        StreamSource streamSource = new StreamSource(FirmwareFactoryTest.class.getResourceAsStream(XSD_LOCATION));
        Schema schema = schemaFactory.newSchema(streamSource);
        unmarshaller.setSchema(schema);

        Reporter.log("Load the firmware sample file.", true);
        InputStream is = ProtocolTest.class.getResourceAsStream("/xml-test/firmware-sample.xml");
        LOGGER.info("Opened stream: {}", is);

        XMLInputFactory factory = XMLInputFactory.newInstance();

        XMLStreamReader xmlr = factory.createXMLStreamReader(is);

        JAXBElement<Firmware> jaxbElement = (JAXBElement<Firmware>) unmarshaller.unmarshal(xmlr, Firmware.class);

        Firmware firmware = (Firmware) jaxbElement.getValue();
        Assert.assertNotNull(firmware);

        FirmwareDefinitionType firmwareDefinition = firmware.getFirmwareDefinition();
        Assert.assertNotNull(firmwareDefinition);

        Assert.assertNotNull(firmwareDefinition.getNode());

        List<NodeType> nodes = firmwareDefinition.getNode();

        NodeType node0 = nodes.get(0);
        Assert.assertNotNull(node0);
        Assert.assertTrue(node0 instanceof DeviceNode);
        Assert.assertEquals(node0.getNodetext().size(), 2);
        Assert.assertEquals(node0.getNode().size(), 3);

        // Assert.assertTrue(node0.getNodetextAndNode().get(0) instanceof NodetextType);
        // Assert.assertTrue(node0.getNodetextAndNode().get(1) instanceof NodetextType);
        Assert.assertTrue(node0.getNode().get(0) instanceof FirmwareNode);
        Assert.assertTrue(node0.getNode().get(1) instanceof FirmwareNode);
        Assert.assertTrue(node0.getNode().get(2) instanceof SimpleNode);
    }
}
