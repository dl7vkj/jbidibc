package org.bidib.jbidibc.vendorcv;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

public class VendorCvBiDiBS88BridgeTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(VendorCvBiDiBS88BridgeTest.class);

    private static final String JAXB_PACKAGE = "org.bidib.jbidibc.vendorcv";

    private static final String XSD_LOCATION = "/xsd/vendor_cv.xsd";

    @Test
    public void loadNodesTest() throws JAXBException, SAXException {
        LOGGER.info("Load CV file for BiDiB-S88-Bridge.");

        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        StreamSource streamSource =
            new StreamSource(VendorCvBiDiBS88BridgeTest.class.getResourceAsStream(XSD_LOCATION));
        Schema schema = schemaFactory.newSchema(streamSource);
        unmarshaller.setSchema(schema);

        Reporter.log("Load the CV file.", true);
        InputStream is = VendorCvBiDiBS88BridgeTest.class.getResourceAsStream("/bidib/BiDiBCV-13-105.xml");
        LOGGER.info("Opened stream: {}", is);
        VendorCV vendorCV = (VendorCV) unmarshaller.unmarshal(is);

        Assert.assertNotNull(vendorCV);

        // LOGGER.info("Loaded VendorCV for GBMboost: {}", vendorCV);

        VersionInfoType versionInfo = vendorCV.getVersion();
        Assert.assertNotNull(versionInfo);

        LOGGER.info("The current version info: {}", versionInfo);

        TemplatesType templates = vendorCV.getTemplates();

        Map<String, Object> templatesMap = new HashMap<String, Object>();
        if (templates != null) {
            for (TemplateType template : templates.getTemplate()) {
                templatesMap.put(template.getName(), template);
            }
        }

        LOGGER.info("Prepared templatesMap: {}", templatesMap);

        CVDefinitionType cvDefinition = vendorCV.getCVDefinition();
        Assert.assertNotNull(cvDefinition);

        List<NodeType> nodes = cvDefinition.getNode();
        Assert.assertNotNull(nodes);

        for (NodeType node : nodes) {
            Assert.assertNotNull(node);
            LOGGER.info("Process current node: {}", node);

            String templateId = node.getTemplate();
            LOGGER.debug("Current templateId: {}", templateId);

            if (StringUtils.isNotBlank(templateId)) {
                Object template = templatesMap.get(templateId);
                LOGGER.debug("Fetched template: {}", template);
                Assert.assertNotNull(template);
            }
            else {
                LOGGER.debug("Current node has no template assigned.");
            }
        }

    }

}
