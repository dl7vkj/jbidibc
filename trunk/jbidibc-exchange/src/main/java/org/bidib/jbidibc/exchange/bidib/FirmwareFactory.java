package org.bidib.jbidibc.exchange.bidib;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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

import org.apache.commons.io.FilenameUtils;
import org.bidib.jbidibc.exchange.firmware.Firmware;
import org.bidib.jbidibc.exchange.firmware.NodetextType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class FirmwareFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(FirmwareFactory.class);

    private static final String JAXB_PACKAGE = "org.bidib.jbidibc.exchange.firmware";

    private static final String XSD_LOCATION = "/xsd/firmware.xsd";

    protected FirmwareFactory() {

    }

    public static Firmware getFirmware(String path) {
        LOGGER.info("Load the Firmware Update info from path: {}", path);

        return new FirmwareFactory().loadFirmwareConfiguration(path);
    }

    public static Firmware getFirmware(StringBuffer definition) {
        LOGGER.info("Load the Firmware Update info from definition: {}", definition);

        return new FirmwareFactory().loadFirmwareConfiguration(definition);
    }

    public static List<String> getFirmwareContent(File firmwareFile, String fileName) {
        LOGGER.info("Load the Firmware content from firmwareFile: {}, fileName: {}", firmwareFile, fileName);

        return new FirmwareFactory().loadFirmwareContentFile(firmwareFile, fileName);
    }

    protected Firmware loadFirmwareConfiguration(StringBuffer definition) {
        LOGGER.info("Load the firmware definition: {}", definition);
        ByteArrayInputStream bais = new ByteArrayInputStream(definition.toString().getBytes());

        Firmware firmware = loadFirmwareFile(bais);
        return firmware;
    }

    protected Firmware loadFirmwareConfiguration(String path) {
        LOGGER.info("Load the firmware definition, path: {}", path);
        Firmware firmware = null;
        File firmwareFile = new File(path);
        if (firmwareFile.exists()) {
            LOGGER.info("Found firmware file: {}", firmwareFile.getAbsolutePath());
            // try to load products
            firmware = loadFirmwareConfigurationFile(firmwareFile);
        }
        else {
            LOGGER.info("File does not exist: {}", firmwareFile.getAbsolutePath());
        }
        return firmware;
    }

    private Firmware loadFirmwareConfigurationFile(File firmwareFile) {

        Firmware firmware = null;
        InputStream is = null;

        if (FilenameUtils.isExtension(firmwareFile.getPath(), "zip")) {

            ZipFile zipFile = null;
            try {
                zipFile = new ZipFile(firmwareFile);
                ZipEntry zipEntry = zipFile.getEntry("firmware.xml");
                if (zipEntry != null) {
                    is = zipFile.getInputStream(zipEntry);
                    firmware = loadFirmwareFile(is);
                }
            }
            catch (Exception e) {
                LOGGER.warn("Check for firmware.xml inside ZIP failed.", e);
            }
            finally {
                if (is != null) {
                    try {
                        is.close();
                    }
                    catch (Exception e) {
                        LOGGER.warn("Close inputStream failed.", e);
                    }
                    is = null;
                }
                if (zipFile != null) {
                    try {
                        zipFile.close();
                    }
                    catch (Exception e) {
                        LOGGER.warn("Close zip failed.", e);
                    }
                }
            }
        }
        else {
            try {
                is = new FileInputStream(firmwareFile);
                firmware = loadFirmwareFile(is);
            }
            catch (FileNotFoundException ex) {
                LOGGER.info("No firmware file found.");
            }
        }
        return firmware;
    }

    private List<String> loadFirmwareContentFile(File firmwareFile, String fileName) {

        List<String> firmwareContent = null;
        InputStream is = null;

        if (FilenameUtils.isExtension(firmwareFile.getPath(), "zip")) {

            ZipFile zipFile = null;
            try {
                zipFile = new ZipFile(firmwareFile);
                ZipEntry zipEntry = zipFile.getEntry(fileName);
                if (zipEntry != null) {
                    is = zipFile.getInputStream(zipEntry);
                    BufferedReader input = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    firmwareContent = new ArrayList<String>();
                    String line = null;
                    while ((line = input.readLine()) != null) {
                        firmwareContent.add(line.trim());
                    }
                }
            }
            catch (Exception e) {
                LOGGER.warn("Check for firmware content inside ZIP failed.", e);
            }
            finally {
                if (is != null) {
                    try {
                        is.close();
                    }
                    catch (Exception e) {
                        LOGGER.warn("Close inputStream failed.", e);
                    }
                    is = null;
                }
                if (zipFile != null) {
                    try {
                        zipFile.close();
                    }
                    catch (Exception e) {
                        LOGGER.warn("Close zip failed.", e);
                    }
                }
            }
        }
        else {
            BufferedReader input = null;
            try {
                input = new BufferedReader(new FileReader(firmwareFile));
                String line = null;
                firmwareContent = new ArrayList<String>();
                while ((line = input.readLine()) != null) {
                    firmwareContent.add(line.trim());
                }
            }
            catch (IOException ex) {
                LOGGER.info("No firmware content file found.");
            }
            finally {
                if (input != null) {
                    try {
                        input.close();
                    }
                    catch (Exception e) {
                        LOGGER.warn("Close reader failed.", e);
                    }
                    input = null;
                }
            }
        }
        return firmwareContent;
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

    public static class NodetextUtils {
        public static String getText(List<NodetextType> nodeTexts, String lang) {

            for (NodetextType nodetext : nodeTexts) {
                if (nodetext.getLang().equalsIgnoreCase(lang)) {
                    return nodetext.getText();
                }
            }
            return null;
        }
    }

}
