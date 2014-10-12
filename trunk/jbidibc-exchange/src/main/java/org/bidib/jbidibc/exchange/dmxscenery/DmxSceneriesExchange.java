package org.bidib.jbidibc.exchange.dmxscenery;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DmxSceneriesExchange {

    private static final Logger LOGGER = LoggerFactory.getLogger(DmxSceneriesExchange.class);

    private static final String JAXB_PACKAGE = "org.bidib.jbidibc.exchange.dmxscenery";

    private static final String XSD_LOCATION = "/xsd/dmxsceneries.xsd";

    public void saveDmxSceneries(DmxSceneries dmxSceneries, String fileName, boolean gzip) {
        LOGGER.info("Save dmxSceneries content to file: {}, dmxSceneries: {}", fileName, dmxSceneries);
        OutputStream os = null;
        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            // marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, XSD_LOCATION);
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            StreamSource streamSource = new StreamSource(DmxSceneriesExchange.class.getResourceAsStream(XSD_LOCATION));
            Schema schema = schemaFactory.newSchema(streamSource);
            marshaller.setSchema(schema);

            os = new BufferedOutputStream(new FileOutputStream(fileName));
            if (gzip) {
                LOGGER.debug("Use gzip to compress macro.");
                os = new GZIPOutputStream(os);
            }

            marshaller.marshal(dmxSceneries, os);

            os.flush();

            LOGGER.info("Save dmxSceneries content to file passed: {}", fileName);
        }
        catch (Exception ex) {
            // TODO add better exception handling
            LOGGER.warn("Save dmxSceneries failed.", ex);

            throw new RuntimeException("Save dmxSceneries failed.", ex);
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

    public DmxSceneries loadDmxSceneries(String fileName) {
        LOGGER.info("Load dmxSceneries content from file: {}", fileName);

        InputStream is = null;
        DmxSceneries dmxSceneries = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            StreamSource streamSource = new StreamSource(DmxSceneriesExchange.class.getResourceAsStream(XSD_LOCATION));
            Schema schema = schemaFactory.newSchema(streamSource);
            unmarshaller.setSchema(schema);

            File importFile = new File(fileName);
            is = new FileInputStream(importFile);

            dmxSceneries = (DmxSceneries) unmarshaller.unmarshal(is);
            LOGGER.info("Loaded dmxSceneries from file: {}", fileName);
        }
        catch (Exception ex) {
            // TODO add better exception handling
            LOGGER.warn("Load dmxSceneries failed.", ex);

            throw new RuntimeException("Load dmxSceneries failed.", ex);
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (IOException ex) {
                    LOGGER.warn("Close inputstream failed.", ex);
                }
            }
        }

        return dmxSceneries;
    }
}
