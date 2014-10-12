package org.bidib.jbidibc.vendorcv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.bidib.jbidibc.core.Node;
import org.bidib.jbidibc.core.utils.NodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class VendorCvFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(VendorCvFactory.class);

    private static final String JAXB_PACKAGE = "org.bidib.jbidibc.vendorcv";

    public static final String XSD_LOCATION = "/xsd/vendor_cv.xsd";

    public static VendorCV getCvDefinition(Node node, String... searchPaths) {
        LOGGER.info("Load the vendor cv definition for node: {}", node);

        return new VendorCvFactory().loadCvDefintionForNode(node.getUniqueId(), searchPaths);
    }

    private VendorCV loadCvDefintionForNode(long uniqueId, String... searchPaths) {
        long pid = NodeUtils.getPid(uniqueId);
        long vid = NodeUtils.getVendorId(uniqueId);
        LOGGER.info("Load the vendor cv definition for uniqueId: {}, pid: {}, vid: {}",
            NodeUtils.getUniqueIdAsString(uniqueId), pid, vid);

        VendorCV vendorCV = null;
        for (String searchPath : searchPaths) {
            StringBuffer filename = new StringBuffer("BiDiBCV-");
            filename.append(vid).append("-").append(pid).append(".xml");

            LOGGER.info("Prepared filename to load vendorCv: {}", filename.toString());
            if (searchPath.startsWith("classpath:")) {
                int beginIndex = "classpath:".length();
                String lookup = searchPath.substring(beginIndex) + "/" + filename.toString();
                LOGGER.info("Lookup vendorCv file internally: {}", lookup);
                InputStream is = VendorCvFactory.class.getResourceAsStream(lookup);
                if (is != null) {
                    vendorCV = loadVendorCvFile(is);
                    if (vendorCV != null) {
                        break;
                    }
                }
                else {
                    LOGGER.warn("Internal lookup for products file failed.");
                }
            }
            else {
                File productsFile = new File(searchPath, filename.toString());
                if (productsFile.exists()) {
                    LOGGER.info("Found product file: {}", productsFile.getAbsolutePath());
                    // try to load products
                    vendorCV = loadVendorCvFile(productsFile);
                    if (vendorCV != null) {
                        break;
                    }
                }
                else {
                    LOGGER.info("File does not exist: {}", productsFile.getAbsolutePath());
                }
            }
        }
        LOGGER.trace("Loaded vendorCV: {}", vendorCV);
        return vendorCV;
    }

    private VendorCV loadVendorCvFile(File vendorCvFile) {
        VendorCV vendorCV = null;
        InputStream is;
        try {
            is = new FileInputStream(vendorCvFile);
            vendorCV = loadVendorCvFile(is);
        }
        catch (FileNotFoundException ex) {
            LOGGER.info("No vendorCV file found.");
        }
        return vendorCV;
    }

    private VendorCV loadVendorCvFile(InputStream is) {

        VendorCV vendorCV = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            StreamSource streamSource = new StreamSource(VendorCvFactory.class.getResourceAsStream(XSD_LOCATION));
            Schema schema = schemaFactory.newSchema(streamSource);
            unmarshaller.setSchema(schema);

            vendorCV = (VendorCV) unmarshaller.unmarshal(is);
        }
        catch (JAXBException | SAXException ex) {
            LOGGER.warn("Load VendorCV from file failed.", ex);
        }
        return vendorCV;
    }

    public static Integer getNumberOfPorts(String portType, Node node, String... searchPaths) {
        LOGGER.info("Load number of configured port from the vendor cv definition for node: {}", node);

        return new VendorCvFactory().getNumberOfPorts(portType, node.getUniqueId(), searchPaths);
    }

    private Integer getNumberOfPorts(String portType, long uniqueId, String... searchPaths) {
        long pid = NodeUtils.getPid(uniqueId);
        long vid = NodeUtils.getVendorId(uniqueId);
        LOGGER.info("Load the vendor cv definition for uniqueId: {}, pid: {}, vid: {}",
            NodeUtils.getUniqueIdAsString(uniqueId), pid, vid);

        // VendorCV vendorCV = null;
        Integer numberOfPorts = null;
        for (String searchPath : searchPaths) {
            StringBuffer filename = new StringBuffer("BiDiBCV-");
            filename.append(vid).append("-").append(pid).append(".xml");

            LOGGER.info("Prepared filename to load vendorCv: {}", filename.toString());
            if (searchPath.startsWith("classpath:")) {
                int beginIndex = "classpath:".length();
                String lookup = searchPath.substring(beginIndex) + "/" + filename.toString();
                LOGGER.info("Lookup vendorCv file internally: {}", lookup);
                InputStream is = VendorCvFactory.class.getResourceAsStream(lookup);
                if (is != null) {
                    numberOfPorts = getNumberOfPorts(is, portType);
                    if (numberOfPorts != null) {
                        break;
                    }
                }
                else {
                    LOGGER.warn("Internal lookup for products file failed.");
                }
            }
            else {
                File productsFile = new File(searchPath, filename.toString());
                if (productsFile.exists()) {
                    LOGGER.info("Found product file: {}", productsFile.getAbsolutePath());
                    // try to load products
                    numberOfPorts = getNumberOfPorts(productsFile, portType);
                    if (numberOfPorts != null) {
                        break;
                    }
                }
                else {
                    LOGGER.info("File does not exist: {}", productsFile.getAbsolutePath());
                }
            }
        }
        LOGGER.trace("Loaded numberOfPorts: {}", numberOfPorts);
        return numberOfPorts;
    }

    public Integer getNumberOfPorts(InputStream is, String portType) {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document xmlDocument = null;
        try {
            builder = builderFactory.newDocumentBuilder();

            xmlDocument = builder.parse(is);
        }
        catch (Exception e) {
            LOGGER.warn("Parse document failed.", e);
        }

        Integer numberOfPorts = null;
        XPath xPath = XPathFactory.newInstance().newXPath();
        String expression = "VendorCV/CVDefinition/Node/Node[@Template='" + portType + "']/@Count";
        try {
            String configuredPorts = xPath.compile(expression).evaluate(xmlDocument/* , XPathConstants.NODE */);
            LOGGER.info("Found requested node: {}", configuredPorts);
            numberOfPorts = Integer.valueOf(configuredPorts);
        }
        catch (XPathExpressionException e) {
            LOGGER.warn("Get number of port failed.", e);
        }

        return numberOfPorts;
    }

    public Integer getNumberOfPorts(File vendorCvFile, String portType) {
        Integer numberOfPorts = null;
        InputStream is;
        try {
            is = new FileInputStream(vendorCvFile);
            numberOfPorts = getNumberOfPorts(is, portType);
        }
        catch (FileNotFoundException ex) {
            LOGGER.info("No vendorCV file found.");
        }
        return numberOfPorts;
    }
}
