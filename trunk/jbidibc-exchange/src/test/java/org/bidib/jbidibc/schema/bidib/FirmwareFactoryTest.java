package org.bidib.jbidibc.schema.bidib;

import java.net.URL;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.bidib.jbidibc.schema.bidib.FirmwareFactory.NodetextUtils;
import org.bidib.schema.firmware.DeviceNode;
import org.bidib.schema.firmware.Firmware;
import org.bidib.schema.firmware.FirmwareDefinitionType;
import org.bidib.schema.firmware.FirmwareNode;
import org.bidib.schema.firmware.NodeType;
import org.bidib.schema.firmware.SimpleNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

public class FirmwareFactoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FirmwareFactoryTest.class);

    @Test
    public void loadFirmwareDefinition() throws JAXBException, SAXException, XMLStreamException {
        LOGGER.info("Prepare the JAXBContext to load the firmware defintion file.");

        URL url = ProtocolTest.class.getResource("/xml-test/firmware-sample.xml");
        Firmware firmware = FirmwareFactory.getFirmware(url.getPath());
        Assert.assertNotNull(firmware);

        FirmwareDefinitionType firmwareDefinition = firmware.getFirmwareDefinition();
        Assert.assertNotNull(firmwareDefinition);

        Assert.assertNotNull(firmwareDefinition.getNode());

        List<NodeType> nodes = firmwareDefinition.getNode();

        NodeType node0 = nodes.get(0);
        Assert.assertNotNull(node0);
        Assert.assertTrue(node0 instanceof DeviceNode);

        DeviceNode deviceNode = (DeviceNode) node0;
        Assert.assertEquals(deviceNode.getNodetext().size(), 2);
        Assert.assertEquals(deviceNode.getNode().size(), 3);
        Assert.assertEquals(deviceNode.getVID(), "013");
        Assert.assertEquals(deviceNode.getPID(), "201");
        // we expect comment
        Assert.assertNotNull(deviceNode.getComment(), "Comment expected!");

        Assert.assertTrue(node0.getNode().get(0) instanceof FirmwareNode);
        Assert.assertTrue(node0.getNode().get(1) instanceof FirmwareNode);
        Assert.assertTrue(node0.getNode().get(2) instanceof SimpleNode);
    }

    @Test
    public void loadFirmwareDefinitionMinimum() throws JAXBException, SAXException, XMLStreamException {
        LOGGER.info("Prepare the JAXBContext to load the firmware defintion file.");

        URL url = ProtocolTest.class.getResource("/xml-test/firmware-minimum.xml");
        Firmware firmware = FirmwareFactory.getFirmware(url.getPath());
        Assert.assertNotNull(firmware);

        FirmwareDefinitionType firmwareDefinition = firmware.getFirmwareDefinition();
        Assert.assertNotNull(firmwareDefinition);

        Assert.assertNotNull(firmwareDefinition.getNode());

        List<NodeType> nodes = firmwareDefinition.getNode();

        NodeType node0 = nodes.get(0);
        Assert.assertNotNull(node0);
        Assert.assertTrue(node0 instanceof DeviceNode);

        DeviceNode deviceNode = (DeviceNode) node0;
        Assert.assertEquals(deviceNode.getNodetext().size(), 2);
        Assert.assertEquals(deviceNode.getNode().size(), 3);
        Assert.assertEquals(deviceNode.getVID(), "013");
        Assert.assertEquals(deviceNode.getPID(), "201");
        Assert.assertNull(deviceNode.getComment(), "No comment expected!");

        Assert.assertTrue(node0.getNode().get(0) instanceof FirmwareNode);
        Assert.assertTrue(node0.getNode().get(1) instanceof FirmwareNode);
        Assert.assertTrue(node0.getNode().get(2) instanceof SimpleNode);
    }

    @Test
    public void loadFirmwareDefinitionBasic() throws JAXBException, SAXException, XMLStreamException {
        LOGGER.info("Prepare the JAXBContext to load the firmware defintion file.");

        URL url = ProtocolTest.class.getResource("/xml-test/firmware-basic.xml");
        Firmware firmware = FirmwareFactory.getFirmware(url.getPath());
        Assert.assertNotNull(firmware);

        FirmwareDefinitionType firmwareDefinition = firmware.getFirmwareDefinition();
        Assert.assertNotNull(firmwareDefinition);

        Assert.assertNotNull(firmwareDefinition.getNode());

        List<NodeType> nodes = firmwareDefinition.getNode();
        Assert.assertNotNull(nodes);
        Assert.assertEquals(nodes.size(), 2);

        NodeType node0 = nodes.get(0);
        Assert.assertNotNull(node0);
        Assert.assertTrue(node0 instanceof DeviceNode);

        DeviceNode deviceNode = (DeviceNode) node0;
        Assert.assertEquals(deviceNode.getNodetext().size(), 2);

        Assert.assertEquals(NodetextUtils.getText(deviceNode.getNodetext(), "de-DE"), "StepControl: Neuinstallation");
        Assert.assertEquals(NodetextUtils.getText(deviceNode.getNodetext(), "en-EN"),
            "StepControl: Initial installation");

        Assert.assertEquals(deviceNode.getNode().size(), 3);
        Assert.assertEquals(deviceNode.getVID(), "013");
        Assert.assertEquals(deviceNode.getPID(), "201");
        Assert.assertNull(deviceNode.getComment(), "No comment expected!");

        // get the update node
        deviceNode = (DeviceNode) nodes.get(1);
        Assert.assertEquals(deviceNode.getNodetext().size(), 2);

        Assert.assertEquals(NodetextUtils.getText(deviceNode.getNodetext(), "de-DE"),
            "StepControl: Update Installation");
        Assert.assertEquals(NodetextUtils.getText(deviceNode.getNodetext(), "en-EN"),
            "StepControl: Update installation");

    }
}
