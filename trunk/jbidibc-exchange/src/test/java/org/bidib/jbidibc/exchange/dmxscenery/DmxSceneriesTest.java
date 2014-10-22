package org.bidib.jbidibc.exchange.dmxscenery;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

public class DmxSceneriesTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DmxSceneriesTest.class);

    private static final String JAXB_PACKAGE = "org.bidib.jbidibc.exchange.dmxscenery";

    private static final String XSD_LOCATION = "/xsd/dmxsceneries.xsd";

    private static final String OUTPUT_TARGET_DIR = "target/dmxsceneries";

    private static final String OUTPUT_FILENAME = "dmxSceneries.xml";

    @BeforeTest
    public void prepareOutputDirectory() {
        try {
            File outputDir = new File(OUTPUT_TARGET_DIR);
            if (!outputDir.exists()) {
                outputDir.mkdir();
            }
            else {
                FileUtils.cleanDirectory(outputDir);
            }
        }
        catch (Exception ex) {
            LOGGER.warn("Create output directory failed: {}", OUTPUT_TARGET_DIR, ex);
        }
    }

    @Test
    public void saveDmxSceneries() throws JAXBException, IOException, SAXException {
        LOGGER.info("Save the DMX sceneries.");

        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        // marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, XSD_LOCATION);
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        StreamSource streamSource = new StreamSource(DmxSceneriesTest.class.getResourceAsStream(XSD_LOCATION));
        Schema schema = schemaFactory.newSchema(streamSource);
        marshaller.setSchema(schema);

        LightPortType lightPort1 = new LightPortType().withPortNumber(1);
        LightPortType lightPort2 = new LightPortType().withPortNumber(2);

        BacklightPortType backlightPort0 = new BacklightPortType().withPortNumber(0);
        BacklightPortType backlightPort1 = new BacklightPortType().withPortNumber(1);
        BacklightPortType backlightPort2 = new BacklightPortType().withPortNumber(2);

        List<DmxChannelType> dmxChannels1 = new LinkedList<>();
        ColorType lineColor1 = new ColorType().withRed(255).withGreen(55).withBlue(55);
        DmxChannelType dmxChannel1 =
            new DmxChannelType()
                .withChannelNumber(1).withLineColor(lineColor1).withPort(lightPort1, lightPort2, backlightPort1);
        dmxChannels1.add(dmxChannel1);

        DmxSceneryType dmxScenery1 = new DmxSceneryType().withSceneryName("Scenery 1").withDmxChannel(dmxChannels1);

        ColorType lineColor2 = new ColorType().withRed(55).withGreen(55).withBlue(255);
        DmxChannelType dmxChannel2 =
            new DmxChannelType()
                .withChannelNumber(2).withLineColor(lineColor2)
                .withPort(lightPort1, lightPort2, backlightPort0, backlightPort2);

        DmxSceneryType dmxScenery2 = new DmxSceneryType().withSceneryName("Scenery 2").withDmxChannel(dmxChannel2);

        DmxSceneries dmxSceneries = new DmxSceneries().withDmxScenery(dmxScenery1, dmxScenery2);

        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(OUTPUT_TARGET_DIR + "/" + OUTPUT_FILENAME));

            marshaller.marshal(dmxSceneries, os);

            os.flush();
        }
        finally {
            if (os != null) {
                try {
                    os.close();
                }
                catch (IOException ex) {
                    LOGGER.warn("Close outputstream failed.", ex);
                }
            }
        }
    }

    @Test
    public void loadDmxSceneries() throws SAXException, JAXBException {
        LOGGER.info("Load the DMX sceneries.");

        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        StreamSource streamSource = new StreamSource(DmxSceneriesTest.class.getResourceAsStream(XSD_LOCATION));
        Schema schema = schemaFactory.newSchema(streamSource);
        unmarshaller.setSchema(schema);

        Reporter.log("Load the DMX sceneries file.", true);
        InputStream is = DmxSceneriesTest.class.getResourceAsStream("/dmxscenery-test/dmxSceneries-test.xml");
        Assert.assertNotNull(is);

        DmxSceneries dmxSceneries = (DmxSceneries) unmarshaller.unmarshal(is);

        Assert.assertNotNull(dmxSceneries);
        LOGGER.info("Loaded dmxSceneries: {}", dmxSceneries);

        Assert.assertNotNull(dmxSceneries.getDmxScenery());
        Assert.assertEquals(dmxSceneries.getDmxScenery().size(), 2);

        Assert.assertNotNull(dmxSceneries.getDmxScenery().get(0));

        DmxSceneryType dmxScenery1 = dmxSceneries.getDmxScenery().get(0);
        Assert.assertEquals(dmxScenery1.getSceneryName(), "Scenery 1");
        Assert.assertNotNull(dmxScenery1.getDmxChannel());
        Assert.assertEquals(dmxScenery1.getDmxChannel().size(), 1);

        DmxChannelType dmxChannel1 = dmxScenery1.getDmxChannel().get(0);
        Assert.assertEquals(dmxChannel1.getChannelName(), "DMX Kanal 1");
        Assert.assertEquals(dmxChannel1.getLineColor(), new ColorType().withRed(255).withGreen(55).withBlue(55));

        Assert.assertNotNull(dmxChannel1.getPort());
        Assert.assertEquals(dmxChannel1.getPort().size(), 3);

        Assert.assertTrue(dmxChannel1.getPort().get(0) instanceof LightPortType);
        LightPortType lightPort1 = (LightPortType) dmxChannel1.getPort().get(0);
        Assert.assertEquals(lightPort1.getPortNumber(), 1);

        Assert.assertTrue(dmxChannel1.getPort().get(1) instanceof LightPortType);
        LightPortType lightPort2 = (LightPortType) dmxChannel1.getPort().get(1);
        Assert.assertEquals(lightPort2.getPortNumber(), 2);

        Assert.assertTrue(dmxChannel1.getPort().get(2) instanceof BacklightPortType);
        BacklightPortType backlightPort1 = (BacklightPortType) dmxChannel1.getPort().get(2);
        Assert.assertEquals(backlightPort1.getPortNumber(), 1);

        DmxSceneryType dmxScenery2 = dmxSceneries.getDmxScenery().get(1);
        Assert.assertEquals(dmxScenery2.getSceneryName(), "Scenery 2");
        Assert.assertNotNull(dmxScenery2.getDmxChannel());
        Assert.assertEquals(dmxScenery2.getDmxChannel().size(), 2);

        DmxChannelType dmxChannel2 = dmxScenery2.getDmxChannel().get(0);
        Assert.assertEquals(dmxChannel2.getChannelName(), "DMX Kanal 2");
        Assert.assertEquals(dmxChannel2.getLineColor(), new ColorType().withRed(55).withGreen(55).withBlue(255));

        DmxChannelType dmxChannel3 = dmxScenery2.getDmxChannel().get(1);
        Assert.assertEquals(dmxChannel3.getChannelName(), "DMX Kanal 3");
        Assert.assertEquals(dmxChannel3.getLineColor(), new ColorType().withRed(55).withGreen(255).withBlue(55));
    }
}
