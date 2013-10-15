package org.bidib.jbidibc.vendorcv;

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

    public static VendorCV getCvDefinition(Node node) {
        LOGGER.info("Load the vendor cv definition for node: {}", node);

        try {
            return loadCvDefintionForNode(node.getUniqueId());
        }
        catch (JAXBException ex) {
            LOGGER.warn("Load CV defintion failed for node: " + node, ex);
        }

        // TODO make better exception here ...
        throw new RuntimeException("Load CV defintion failed.");
    }

    private static VendorCV loadCvDefintionForNode(long uniqueId) throws JAXBException {
        long pid = NodeUtils.getPid(uniqueId);
        long vid = NodeUtils.getVendorId(uniqueId);
        LOGGER.info("Load the vendor cv definition for uniqueId: {}, pid: {}, vid: {}", uniqueId, pid, vid);

        StringBuffer filename = new StringBuffer("/bidib/BiDiBCV-");
        filename.append(vid).append("-").append(pid).append(".xml");

        LOGGER.info("Prepared filename to load vendor cv: {}", filename.toString());

        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        VendorCV vendorCV = null;
        InputStream is = VendorCvFactory.class.getResourceAsStream(filename.toString());
        if (is != null) {
            vendorCV = (VendorCV) unmarshaller.unmarshal(is);
        }
        LOGGER.debug("Loaded vendorCV: {}", vendorCV);
        return vendorCV;
    }
}
