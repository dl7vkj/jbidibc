package org.bidib.jbidibc.vendorcv;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.util.JAXBResult;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

public class VendorCVTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(VendorCVTest.class);

    private static final String JAXB_PACKAGE = "org.bidib.jbidibc.vendorcv";

    private static final String XSD_LOCATION = "xsd/vendor_cv.xsd";

    private static final String EXPORTED_CVDEF_TARGET_DIR = "target/vendorcv";

    @BeforeTest
    public void prepare() {
        LOGGER.info("Create report directory: {}", EXPORTED_CVDEF_TARGET_DIR);

        try {
            FileUtils.forceMkdir(new File(EXPORTED_CVDEF_TARGET_DIR));
        }
        catch (IOException e) {
            LOGGER.warn("Create report directory failed: " + EXPORTED_CVDEF_TARGET_DIR, e);
        }
    }

    private InputStream getInputStream(String filePath) throws FileNotFoundException {
        File file = new File("");
        file = new File(file.getAbsoluteFile(), filePath);
        LOGGER.info("Prepared file: {}", file.getAbsolutePath());

        InputStream is = new FileInputStream(file);
        return is;
    }

    @Test
    public void loadVendorCVForOneDMXAndTransformToWizardFormatTest() throws JAXBException, TransformerException,
        IOException {
        LOGGER.info("Load VendorCV for OneDMX and transform to wizard data.");

        // System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");

        InputStream xsltStream = VendorCVTest.class.getResourceAsStream("/xsd/vendor_cv.xslt");
        javax.xml.transform.Source xsltSource = new javax.xml.transform.stream.StreamSource(xsltStream);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer(xsltSource);

        InputStream is = getInputStream("src/main/xml/bidib/BiDiBCV-13-115.xml");
        Assert.assertNotNull(is);
        StreamSource xmlSource = new StreamSource(is);

        /*
         * Perform the transform to get the report
         */
        File exportFile = new File(EXPORTED_CVDEF_TARGET_DIR, "vendorcv_wizard_OneDMX.xml");

        FileOutputStream reportFOS = new FileOutputStream(exportFile);
        OutputStreamWriter osw = new OutputStreamWriter(reportFOS, "UTF-8");
        transformer.transform(xmlSource, new StreamResult(osw));
        osw.close();

        LOGGER.info("Prepared wizard xml file: {}", exportFile.getPath());
    }

    @Test
    public void loadVendorForLCAndTransformCVTest() throws JAXBException, TransformerException, FileNotFoundException {
        LOGGER.info("Load VendorCV.");

        // System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");

        InputStream xsltStream = VendorCVTest.class.getResourceAsStream("/xsd/vendor_cv.xslt");
        javax.xml.transform.Source xsltSource = new javax.xml.transform.stream.StreamSource(xsltStream);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer(xsltSource);

        InputStream is = getInputStream("src/main/xml/bidib/BiDiBCV-13-107.xml");
        Assert.assertNotNull(is);

        LOGGER.info("Loaded LC config: {}", is);
        StreamSource xmlSource = new StreamSource(is);

        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

        JAXBResult outputTarget = new JAXBResult(jaxbContext);
        transformer.transform(xmlSource, outputTarget);

        LOGGER.info("Prepared outputTarget: {}", outputTarget);

        VendorCV vendorCV = (VendorCV) outputTarget.getResult();
        Assert.assertNotNull(vendorCV);

        Assert.assertNotNull(vendorCV.getVersion());
        VersionInfoType versionInfo = vendorCV.getVersion();
        Assert.assertEquals(versionInfo.getVersion(), "0.2");
        Assert.assertEquals(versionInfo.getVendor(), "013");

        Assert.assertNotNull(vendorCV.getTemplates());
        Assert.assertEquals(vendorCV.getTemplates().getTemplate().size(), 2);

        // check the led values
        TemplateType ledTemplate = vendorCV.getTemplates().getTemplate().get(0);
        Assert.assertNotNull(ledTemplate);
        Assert.assertEquals(ledTemplate.getName(), "LED");
        Assert.assertNotNull(ledTemplate.getCV());
        Assert.assertEquals(ledTemplate.getCV().size(), 5);
        Assert.assertNotNull(ledTemplate.getCV().get(0));
        Assert.assertEquals(ledTemplate.getCV().get(0).getNumber(), 0);
        Assert.assertEquals(ledTemplate.getCV().get(0).getType(), DataType.BYTE);
        Assert.assertEquals(ledTemplate.getCV().get(0).getMin(), "-");

        // check the servo values
        TemplateType servoTemplate = vendorCV.getTemplates().getTemplate().get(1);
        Assert.assertNotNull(servoTemplate);
        Assert.assertEquals(servoTemplate.getName(), "Servo");
        Assert.assertNotNull(servoTemplate.getCV());
        Assert.assertEquals(servoTemplate.getCV().size(), 13);

        Assert.assertNotNull(vendorCV.getCVDefinition());
        CVDefinitionType cvDefinition = vendorCV.getCVDefinition();
        Assert.assertNotNull(cvDefinition.getNode());
        Assert.assertEquals(cvDefinition.getNode().size(), 3);
        // common data
        Assert.assertNotNull(cvDefinition.getNode().get(0));
        NodeType commonData = cvDefinition.getNode().get(0);
        Assert.assertNotNull(commonData);
        Assert.assertNull(commonData.getTemplate());
        Assert.assertNotNull(commonData.getNodetextAndNodeAndCV());
        Assert.assertEquals(commonData.getNodetextAndNodeAndCV().size(), 14);

        // servos
        Assert.assertNotNull(cvDefinition.getNode().get(1));
        NodeType servosData = cvDefinition.getNode().get(1);
        Assert.assertNotNull(servosData.getNodetextAndNodeAndCV());
        Assert.assertEquals(servosData.getNodetextAndNodeAndCV().size(), 3);

        NodeType servoNode = (NodeType) servosData.getNodetextAndNodeAndCV().get(2);
        Assert.assertEquals(servoNode.getTemplate(), "Servo");

        // leds
        Assert.assertNotNull(cvDefinition.getNode().get(2));
        NodeType ledsData = cvDefinition.getNode().get(2);
        Assert.assertNotNull(ledsData.getNodetextAndNodeAndCV());
        Assert.assertEquals(ledsData.getNodetextAndNodeAndCV().size(), 3);

        NodeType ledNode = (NodeType) ledsData.getNodetextAndNodeAndCV().get(2);
        Assert.assertEquals(ledNode.getTemplate(), "LED");
    }

    @Test
    public void loadVendorCVForS88BridgeAndTransformToWizardFormatTest() throws JAXBException, TransformerException,
        IOException {
        LOGGER.info("Load VendorCV for S88Bridge and transform to wizard data.");

        // System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");

        InputStream xsltStream = VendorCVTest.class.getResourceAsStream("/xsd/vendor_cv.xslt");
        javax.xml.transform.Source xsltSource = new javax.xml.transform.stream.StreamSource(xsltStream);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer(xsltSource);
        // transformer.setOutputProperty(OutputKeys.INDENT, "no");

        InputStream is = getInputStream("src/main/xml/bidib/BiDiBCV-13-105.xml");
        Assert.assertNotNull(is);

        StreamSource xmlSource = new StreamSource(is);

        /*
         * Perform the transform to get the report
         */
        File exportFile = new File(EXPORTED_CVDEF_TARGET_DIR, "vendorcv_wizard_S88Bridge.xml");

        FileOutputStream reportFOS = new FileOutputStream(exportFile);
        OutputStreamWriter osw = new OutputStreamWriter(reportFOS, "UTF-8");
        transformer.transform(xmlSource, new StreamResult(osw));
        osw.close();

        LOGGER.info("Prepared wizard xml file: {}", exportFile.getPath());
    }

    @Test
    public void saveVendorCVS88BridgeTest() throws JAXBException, SAXException, IOException {
        LOGGER.info("Save vendor CV for S88Bridge.");
        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, XSD_LOCATION);

        VersionInfoType versionInfo = new VersionInfoType();
        versionInfo.setAuthor("Max Mustermann");
        versionInfo.setVersion("0.1");
        versionInfo.setVendor("013");

        VendorCV vendorCV = new VendorCV();
        vendorCV.setVersion(versionInfo);

        CVDefinitionType cvDefinition = new CVDefinitionType();
        // Basis basis = new Basis();
        // CV cv = new CV();
        // cv.setNumber(1);
        // cv.setType(DataType.BYTE);
        // cv.setMin("0");
        // cv.setMax("255");
        // cv.setLow("-");
        // cv.setHigh("-");
        // cv.setValues("-");
        // cv.setMode(ModeType.RO);
        // // TODO
        // // cv.setDescde("vendor_id");
        // // cv.setDescen("vendor_id");
        // basis.getCV().add(cv);
        //
        // cv = new CV();
        // cv.setNumber(2);
        // cv.setType(DataType.BYTE);
        // cv.setMin("0");
        // cv.setMax("255");
        // cv.setLow("-");
        // cv.setHigh("-");
        // cv.setValues("-");
        // cv.setMode(ModeType.RO);
        // // TODO
        // // cv.setDescde("hw_id");
        // // cv.setDescen("hw_id");
        // basis.getCV().add(cv);
        //
        // cvDefinition.setBasis(basis);
        // vendorCV.setCVDefinition(cvDefinition);
        //
        // File exportFile = new File(EXPORTED_CVDEF_TARGET_DIR, "vendorcv_S88Bridge.xml");
        // OutputStream os = new BufferedOutputStream(new FileOutputStream(exportFile));
        //
        // marshaller.marshal(vendorCV, os);
        //
        // os.flush();
    }

    @Test
    public void saveVendorCVLCTest() throws JAXBException, SAXException, IOException {

        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, XSD_LOCATION);

        VersionInfoType versionInfo = new VersionInfoType();
        versionInfo.setAuthor("Max Mustermann");
        versionInfo.setVersion("0.1");
        versionInfo.setVendor("013");
        versionInfo.setPid("107");
        versionInfo.setDescription("BiDiB-LightControl 1");

        VendorCV vendorCV = new VendorCV();
        vendorCV.setVersion(versionInfo);

        TemplatesType templatesType = new TemplatesType();

        TemplateType ledTemplate = new TemplateType();
        ledTemplate.setName("LED");

        templatesType.getTemplate().add(ledTemplate);

        CVType cv = new CVType();
        cv.setNumber(0);
        cv.setType(DataType.BYTE);
        cv.setMode(ModeType.RW);
        DescriptionType desc = new DescriptionType().withLang("de-DE").withText("LED: Einstellung der Stromquelle");
        cv.getDescription().add(desc);
        desc = new DescriptionType().withLang("en-EN").withText("LED: courrent source setup");
        cv.getDescription().add(desc);
        cv.setMin("-");
        cv.setMax("-");
        cv.setLow("-");
        cv.setHigh("-");
        cv.setValues("-");
        ledTemplate.getCV().add(cv);

        cv = new CVType();
        cv.setNumber(1);
        cv.setType(DataType.BYTE);
        cv.setMode(ModeType.RW);
        desc = new DescriptionType().withLang("de-DE").withText("LED: Helligkeit für Zustand „aus“");
        cv.getDescription().add(desc);
        desc = new DescriptionType().withLang("en-EN").withText("LED: light intensity at status 'off'");
        cv.getDescription().add(desc);
        cv.setMin("-");
        cv.setMax("-");
        cv.setLow("-");
        cv.setHigh("-");
        cv.setValues("-");
        ledTemplate.getCV().add(cv);

        TemplateType servoTemplate = new TemplateType();
        servoTemplate.setName("Servo");

        templatesType.getTemplate().add(servoTemplate);

        cv = new CVType();
        cv.setNumber(0);
        cv.setType(DataType.INT);
        cv.setMode(ModeType.RW);
        desc = new DescriptionType().withLang("de-DE").withText("Min Low");
        cv.getDescription().add(desc);
        desc = new DescriptionType().withLang("en-EN").withText("Min Low");
        cv.getDescription().add(desc);
        cv.setMin("-");
        cv.setMax("-");
        cv.setLow("0");
        cv.setHigh("1");
        cv.setValues("-");
        servoTemplate.getCV().add(cv);

        cv = new CVType();
        cv.setNumber(1);
        cv.setType(DataType.INT);
        cv.setMode(ModeType.RW);
        desc = new DescriptionType().withLang("de-DE").withText("Min High");
        cv.getDescription().add(desc);
        desc = new DescriptionType().withLang("en-EN").withText("Min High");
        cv.getDescription().add(desc);
        cv.setMin("-");
        cv.setMax("-");
        cv.setLow("0");
        cv.setHigh("1");
        cv.setValues("-");
        servoTemplate.getCV().add(cv);

        vendorCV.setTemplates(templatesType);

        File exportFile = new File(EXPORTED_CVDEF_TARGET_DIR, "vendorcv_LC1.xml");
        OutputStream os = new BufferedOutputStream(new FileOutputStream(exportFile));

        marshaller.marshal(vendorCV, os);

        os.flush();
    }
}
