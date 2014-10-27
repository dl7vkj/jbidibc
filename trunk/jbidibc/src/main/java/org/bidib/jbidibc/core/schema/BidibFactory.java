package org.bidib.jbidibc.core.schema;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import org.bidib.jbidibc.core.Node;
import org.bidib.jbidibc.core.schema.bidib.BiDiB;
import org.bidib.jbidibc.core.schema.bidib.MessageType;
import org.bidib.jbidibc.core.schema.bidib.Product;
import org.bidib.jbidibc.core.schema.bidib.Products;
import org.bidib.jbidibc.core.utils.NodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class BidibFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(BidibFactory.class);

    private static final String JAXB_PACKAGE = "org.bidib.jbidibc.core.schema.bidib";

    public static final String XSD_LOCATION = "/xsd/bidib.xsd";

    public BidibFactory() {
    }

    public static List<MessageType> getMessageTypes() {

        return new BidibFactory().loadMessageTypes();
    }

    public static Product getProduct(Node node, String... searchPaths) {
        LOGGER.info("Load the Product info for node: {}", node);

        return new BidibFactory().loadProductForNode(node.getUniqueId(), searchPaths);
    }

    protected List<MessageType> loadMessageTypes() {
        final String fileName = "/xml/protocol/Protocol.bidib";

        InputStream is = BidibFactory.class.getResourceAsStream(fileName);
        if (is != null) {
            BiDiB bidib = loadBiDiBFile(is);
            if (bidib != null) {
                try {
                    List<MessageType> messageTypes = bidib.getProtocol().getMessageTypes().getMessageType();
                    LOGGER.info("Loaded number of messageTypes: {}", messageTypes.size());
                    return messageTypes;
                }
                catch (Exception ex) {
                    LOGGER.warn("Get the message types failed.", ex);
                }
            }
        }
        else {
            LOGGER.warn("Load protocol file failed.");
        }
        return null;
    }

    public Product loadProductForNode(long uniqueId, String... searchPaths) {
        long pid = NodeUtils.getPid(uniqueId);
        long vid = NodeUtils.getVendorId(uniqueId);
        LOGGER.info("Load the vendor cv definition for uniqueId: {}, pid: {}, vid: {}",
            NodeUtils.getUniqueIdAsString(uniqueId), pid, vid);

        BiDiB bidib = null;
        for (String searchPath : searchPaths) {
            StringBuffer filename = new StringBuffer("Products_");
            filename.append(vid).append(".bidib");

            LOGGER.info("Prepared filename to load products: {}", filename.toString());
            if (searchPath.startsWith("classpath:")) {
                int beginIndex = "classpath:".length();
                String lookup = searchPath.substring(beginIndex) + "/" + filename.toString();
                LOGGER.info("Lookup products file internally: {}", lookup);
                InputStream is = BidibFactory.class.getResourceAsStream(lookup);
                if (is != null) {
                    bidib = loadBiDiBFile(is);
                    if (bidib != null) {
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
                    bidib = loadBiDiBFile(productsFile);
                    if (bidib != null) {
                        break;
                    }
                }
                else {
                    LOGGER.info("File does not exist: {}", productsFile.getAbsolutePath());
                }
            }
        }
        Product product = null;
        if (bidib != null) {

            Products products = bidib.getProducts();
            if (products != null) {
                short productTypeId = (short) pid;
                for (Product current : products.getProduct()) {
                    if (current.getProductTypeId() == productTypeId) {
                        product = current;
                        break;
                    }
                }
            }
        }

        LOGGER.trace("Loaded product: {}", product);
        return product;
    }

    private BiDiB loadBiDiBFile(File productsFile) {

        BiDiB bidib = null;
        InputStream is;
        try {
            is = new FileInputStream(productsFile);
            bidib = loadBiDiBFile(is);
        }
        catch (FileNotFoundException ex) {
            LOGGER.info("No Products file found.");
        }
        return bidib;
    }

    private BiDiB loadBiDiBFile(InputStream is) {

        BiDiB bidib = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            StreamSource streamSource = new StreamSource(BidibFactory.class.getResourceAsStream(XSD_LOCATION));
            Schema schema = schemaFactory.newSchema(streamSource);
            unmarshaller.setSchema(schema);

            XMLInputFactory factory = XMLInputFactory.newInstance();

            XMLStreamReader xmlr = factory.createXMLStreamReader(is);

            JAXBElement<BiDiB> jaxbElement = (JAXBElement<BiDiB>) unmarshaller.unmarshal(xmlr, BiDiB.class);
            bidib = jaxbElement.getValue();
        }
        catch (JAXBException | XMLStreamException | SAXException ex) {
            LOGGER.warn("Load content from file failed.", ex);
        }
        return bidib;
    }
}
