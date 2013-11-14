package org.bidib.jbidibc.vendorcv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.bidib.jbidibc.Node;
import org.bidib.jbidibc.utils.NodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VendorCvFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(VendorCvFactory.class);

    private static final String JAXB_PACKAGE = "org.bidib.jbidibc.vendorcv";

    public static VendorCV getCvDefinition(Node node, String... searchPaths) {
        LOGGER.info("Load the vendor cv definition for node: {}", node);

        return loadCvDefintionForNode(node.getUniqueId(), searchPaths);
    }

    private static VendorCV loadCvDefintionForNode(long uniqueId, String... searchPaths) {
        long pid = NodeUtils.getPid(uniqueId);
        long vid = NodeUtils.getVendorId(uniqueId);
        LOGGER.info("Load the vendor cv definition for uniqueId: {}, pid: {}, vid: {}", NodeUtils
            .getUniqueIdAsString(uniqueId), pid, vid);

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

    private static VendorCV loadVendorCvFile(File vendorCvFile) {
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

    private static VendorCV loadVendorCvFile(InputStream is) {

        VendorCV vendorCV = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            vendorCV = (VendorCV) unmarshaller.unmarshal(is);
        }
        catch (JAXBException ex) {
            LOGGER.warn("Load VendorCV from file failed.", ex);
        }
        return vendorCV;
    }
}
