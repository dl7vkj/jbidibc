package org.bidib.jbidibc.schema.bidib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.bidib.jbidibc.Node;
import org.bidib.jbidibc.utils.NodeUtils;
import org.bidib.schema.bidib.BiDiB;
import org.bidib.schema.bidib.Product;
import org.bidib.schema.bidib.Products;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BidibFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(BidibFactory.class);

    private static final String JAXB_PACKAGE = "org.bidib.schema.bidib";

    protected BidibFactory() {
    }

    public static Product getProduct(Node node, String[] searchPaths) {
        LOGGER.info("Load the Product info for node: {}", node);

        try {
            return new BidibFactory().loadProductForNode(node.getUniqueId(), searchPaths);
        }
        catch (JAXBException ex) {
            LOGGER.warn("Load Product info failed for node: " + node, ex);
        }

        // TODO make better exception here ...
        throw new RuntimeException("Load Product info failed.");
    }

    protected Product loadProductForNode(long uniqueId, String... searchPaths) throws JAXBException {
        long pid = NodeUtils.getPid(uniqueId);
        long vid = NodeUtils.getVendorId(uniqueId);
        LOGGER.info("Load the vendor cv definition for uniqueId: {}, pid: {}, vid: {}", NodeUtils
            .getUniqueIdAsString(uniqueId), pid, vid);

        BiDiB bidib = null;
        for (String searchPath : searchPaths) {
            StringBuffer filename = new StringBuffer("Products_");
            filename.append(vid).append(".bidib");

            LOGGER.info("Prepared filename to load products: {}", filename.toString());
            File productsFile = new File(searchPath, filename.toString());
            if (productsFile.exists()) {
                // try to load products
                bidib = loadProductsFile(productsFile);
                if (bidib != null) {
                    break;
                }
            }
            else {
                LOGGER.info("File does not exist: {}", productsFile.getAbsolutePath());
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

        LOGGER.debug("Loaded product: {}", product);
        return product;
    }

    private BiDiB loadProductsFile(File productsFile) {

        BiDiB bidib = null;
        //        InputStream is = BidibFactory.class.getResourceAsStream(filename.toString());
        InputStream is;
        try {
            is = new FileInputStream(productsFile);
            JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            JAXBElement<BiDiB> jaxbElement = (JAXBElement<BiDiB>) unmarshaller.unmarshal(is);
            bidib = jaxbElement.getValue();
        }
        catch (FileNotFoundException ex) {
            LOGGER.info("No Products file found.");
        }
        catch (JAXBException ex) {
            LOGGER.warn("Load Products from file failed.", ex);
        }
        return bidib;
    }
}
