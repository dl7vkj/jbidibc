package org.bidib.jbidibc.vendorcv;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class VendorCvOneDmxTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(VendorCvOneDmxTest.class);
    
    private static final String JAXB_PACKAGE = "org.bidib.jbidibc.vendorcv";

    private static final String XSD_LOCATION = "xsd/bidib.xsd";

    
    @Test
    public void loadNodesTest() throws JAXBException {
        LOGGER.info("Load CV file for OneDMX.");

        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        InputStream is = VendorCvOneDmxTest.class.getResourceAsStream("/bidib/BiDiBCV-13-115.xml");
        VendorCV vendorCV = (VendorCV) unmarshaller.unmarshal(is);
        
        Assert.assertNotNull(vendorCV);
        
//        LOGGER.info("Loaded VendorCV for OneDMX: {}", vendorCV);
        
        VersionInfoType versionInfo = vendorCV.getVersion();
        Assert.assertNotNull(versionInfo);
        
        LOGGER.info("The current version info: {}", versionInfo);
        
        TemplatesType templates = vendorCV.getTemplates();
        AboutType about = templates.getAbout();
        
        Map<String, Object> templatesMap = new HashMap<String, Object>();
        if (about != null) {
            templatesMap.put("About", about);
        }
        
        CVDefinitionType cvDefinition = vendorCV.getCVDefinition();
        Assert.assertNotNull(cvDefinition);
        
        List<NodeType> nodes = cvDefinition.getNode();
        Assert.assertNotNull(nodes);
        
        for (NodeType node : nodes) {
            Assert.assertNotNull(node);
            LOGGER.info("Process current node: {}", node);
            
            String templateId = node.getTemplate();
            LOGGER.debug("Current templateId: {}", templateId);
            
            Object template = templatesMap.get(templateId);
            LOGGER.debug("Fetched template: {}", template);
            
            
        }
        
    }

}
