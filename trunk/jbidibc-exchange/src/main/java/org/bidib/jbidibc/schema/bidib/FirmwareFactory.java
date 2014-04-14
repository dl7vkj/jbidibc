package org.bidib.jbidibc.schema.bidib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import org.bidib.schema.firmware.Firmware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class FirmwareFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(FirmwareFactory.class);

    private static final String JAXB_PACKAGE = "org.bidib.schema.firmware";

    private static final String XSD_LOCATION = "/xsd/firmware.xsd";

    protected FirmwareFactory() {

    }

    public static Firmware getFirmware(String path) {
        LOGGER.info("Load the Firmware Update info for node: {}", path);

        return new FirmwareFactory().loadFirmwareConfiguration(path);
    }

    protected Firmware loadFirmwareConfiguration(String path) {
        LOGGER.info("Load the firmware definition, path: {}", path);
        Firmware firmware = null;
        File firmwareFile = new File(path);
        if (firmwareFile.exists()) {
            LOGGER.info("Found firmware file: {}", firmwareFile.getAbsolutePath());
            // try to load products
            firmware = loadFirmwareFile(firmwareFile);
        }
        else {
            LOGGER.info("File does not exist: {}", firmwareFile.getAbsolutePath());
        }
        return firmware;
    }

    private Firmware loadFirmwareFile(File firmwareFile) {

        Firmware firmware = null;
        InputStream is;
        try {
            is = new FileInputStream(firmwareFile);
            firmware = loadFirmwareFile(is);
        }
        catch (FileNotFoundException ex) {
            LOGGER.info("No firmware file found.");
        }
        return firmware;
    }

    private Firmware loadFirmwareFile(InputStream is) {

        Firmware firmware = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            StreamSource streamSource = new StreamSource(FirmwareFactory.class.getResourceAsStream(XSD_LOCATION));
            Schema schema = schemaFactory.newSchema(streamSource);
            unmarshaller.setSchema(schema);

            XMLInputFactory factory = XMLInputFactory.newInstance();

            XMLStreamReader xmlr = factory.createXMLStreamReader(is);

            JAXBElement<Firmware> jaxbElement = (JAXBElement<Firmware>) unmarshaller.unmarshal(xmlr, Firmware.class);
            firmware = jaxbElement.getValue();
        }
        catch (JAXBException | XMLStreamException | SAXException ex) {
            LOGGER.warn("Load firmware from file failed.", ex);
        }
        return firmware;
    }

}
