package org.bidib.jbidibc.exchange.lcmacro;

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

public class LcAccessoryExporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(LcAccessoryExporter.class);

    public static final String JAXB_PACKAGE = "org.bidib.jbidibc.exchange.lcmacro";

    public static final String XSD_LOCATION = "/xsd/accessories.xsd";

    /**
     * Save a single accessory
     * 
     * @param lcAccessory
     *            the accessory
     * @param fileName
     *            the filename
     */
    public void saveAccessory(LcAccessoryType lcAccessory, String fileName, boolean gzip) {
        LOGGER.info("Save accessory content to file: {}, lcMacro: {}", fileName, lcAccessory);
        OutputStream os = null;
        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            // marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, XSD_LOCATION);
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            StreamSource streamSource = new StreamSource(LcAccessoryExporter.class.getResourceAsStream(XSD_LOCATION));
            Schema schema = schemaFactory.newSchema(streamSource);
            marshaller.setSchema(schema);

            LcAccessories lcAccessories = new LcAccessories();
            lcAccessories.setLcAccessory(lcAccessory);

            os = new BufferedOutputStream(new FileOutputStream(fileName));
            if (gzip) {
                LOGGER.debug("Use gzip to compress accessory.");
                os = new GZIPOutputStream(os);
            }

            marshaller.marshal(lcAccessories, os);

            os.flush();

            LOGGER.info("Save accessory content to file passed: {}", fileName);
        }
        catch (Exception ex) {
            // TODO add better exception handling
            LOGGER.warn("Save accessory failed.", ex);

            throw new RuntimeException("Save accessory failed.", ex);
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

    public LcAccessoryType loadAccessory(String fileName) {
        LOGGER.info("Load accessory content from file: {}", fileName);

        // TODO
        InputStream is = null;
        LcAccessories accessories = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            StreamSource streamSource = new StreamSource(LcAccessoryExporter.class.getResourceAsStream(XSD_LOCATION));
            Schema schema = schemaFactory.newSchema(streamSource);
            unmarshaller.setSchema(schema);

            File importFile = new File(fileName);
            is = new FileInputStream(importFile);

            accessories = (LcAccessories) unmarshaller.unmarshal(is);
            LOGGER.info("Loaded accessories from file: {}", fileName);
        }
        catch (Exception ex) {
            // TODO add better exception handling
            LOGGER.warn("Load accessories failed.", ex);

            throw new RuntimeException("Load accessories failed.", ex);
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

        LcAccessoryType lcAccessory = null;
        if (accessories != null) {
            lcAccessory = accessories.getLcAccessory();
            LOGGER.debug("Loaded accessory: {}", lcAccessory);
        }

        return lcAccessory;
    }
}
