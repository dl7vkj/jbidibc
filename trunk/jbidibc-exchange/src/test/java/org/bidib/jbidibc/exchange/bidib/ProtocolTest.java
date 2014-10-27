package org.bidib.jbidibc.exchange.bidib;

import java.io.InputStream;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

public class ProtocolTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProtocolTest.class);

    private static final String JAXB_PACKAGE = "org.bidib.jbidibc.exchange.bidib";

    private static final String XSD_LOCATION = "/xsd/bidib.xsd";

    @Test
    public void loadProtocol() throws JAXBException, SAXException, XMLStreamException {
        LOGGER.info("Prepare the JAXBContext to load the protocol file.");

        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        StreamSource streamSource = new StreamSource(ProtocolTest.class.getResourceAsStream(XSD_LOCATION));
        Schema schema = schemaFactory.newSchema(streamSource);
        unmarshaller.setSchema(schema);

        Reporter.log("Load the protocol file.", true);
        InputStream is = ProtocolTest.class.getResourceAsStream("/xml/protocol/Protocol.bidib");
        LOGGER.info("Opened stream: {}", is);

        XMLInputFactory factory = XMLInputFactory.newInstance();

        XMLStreamReader xmlr = factory.createXMLStreamReader(is);

        JAXBElement<BiDiB> jaxbElement = (JAXBElement<BiDiB>) unmarshaller.unmarshal(xmlr, BiDiB.class);

        BiDiB bidib = (BiDiB) jaxbElement.getValue();
        Assert.assertNotNull(bidib);

        Protocol protocol = bidib.getProtocol();
        Assert.assertNotNull(protocol);

        Assert.assertNotNull(protocol.getMessageTypes());
        Assert.assertEquals(protocol.getMessageTypes().getMessageType().size(), 120);

        Assert.assertNotNull(protocol.getFeatureCodes());
        Assert.assertEquals(protocol.getFeatureCodes().getFeatureCode().size(), 64);

        boolean feature_ctrl_port_query_available = false;
        for (FeatureCode featureCode : protocol.getFeatureCodes().getFeatureCode()) {
            if (featureCode.getName().equals("FEATURE_CTRL_PORT_QUERY_AVAILABLE")) {
                feature_ctrl_port_query_available = true;
                break;
            }
        }

        Assert.assertTrue(feature_ctrl_port_query_available);
    }
}
