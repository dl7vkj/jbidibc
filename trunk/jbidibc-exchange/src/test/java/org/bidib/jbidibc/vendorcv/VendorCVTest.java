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

    @Test
    public void loadVendorCVForOneDMXAndTransformToWizardFormatTest() throws JAXBException, TransformerException,
        IOException {
        LOGGER.info("Load VendorCV for OneDMX and transform to wizard data.");

        System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");

        InputStream xsltStream = VendorCVTest.class.getResourceAsStream("/xsd/vendor_cv.xslt");
        javax.xml.transform.Source xsltSource = new javax.xml.transform.stream.StreamSource(xsltStream);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer(xsltSource);

        InputStream is = VendorCVTest.class.getResourceAsStream("/xsd/monitor-0.4.4.0/BiDiBCV-13-115.xml");
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
    public void loadVendorCVForLCAndTransformToWizardFormatTest() throws JAXBException, TransformerException,
        IOException {
        LOGGER.info("Load VendorCV for LC and transform to wizard data.");

        System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");

        InputStream xsltStream = VendorCVTest.class.getResourceAsStream("/xsd/vendor_cv.xslt");
        javax.xml.transform.Source xsltSource = new javax.xml.transform.stream.StreamSource(xsltStream);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer(xsltSource);

        InputStream is = VendorCVTest.class.getResourceAsStream("/xsd/monitor-0.4.2.3/05.00.0D.6B.00-VendorCV.xml");
        StreamSource xmlSource = new StreamSource(is);

        /*
         * Perform the transform to get the report
         */
        File exportFile = new File(EXPORTED_CVDEF_TARGET_DIR, "vendorcv_wizard_LC.xml");

        FileOutputStream reportFOS = new FileOutputStream(exportFile);
        OutputStreamWriter osw = new OutputStreamWriter(reportFOS, "UTF-8");
        transformer.transform(xmlSource, new StreamResult(osw));
        osw.close();

        LOGGER.info("Prepared wizard xml file: {}", exportFile.getPath());
    }

    // TODO fix and enable test
    @Test(enabled = false)
    public void loadVendorForLCAndTransformCVTest() throws JAXBException, TransformerException, FileNotFoundException {
        LOGGER.info("Load VendorCV.");

        System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");

        InputStream xsltStream = VendorCVTest.class.getResourceAsStream("/xsd/vendor_cv.xslt");
        javax.xml.transform.Source xsltSource = new javax.xml.transform.stream.StreamSource(xsltStream);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer(xsltSource);

        File file = new File("");
        file = new File(file.getAbsoluteFile(), "src/main/xml/bidib/BiDiBCV-13-107.xml");
        LOGGER.info("Prepared file: {}", file.getAbsolutePath());

        InputStream is = new FileInputStream(file);

        //        InputStream is = VendorCVTest.class.getResourceAsStream(file.getAbsolutePath());
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
        Assert.assertEquals(versionInfo.getVersion(), "0.1");
        Assert.assertEquals(versionInfo.getVendor(), "013");

        Assert.assertNotNull(vendorCV.getTemplates());
        // check the led values
        // TODO enable
        Assert.assertNotNull(vendorCV.getTemplates().getTemplate().get(0));
        TemplateType led = vendorCV.getTemplates().getTemplate().get(0);
        //        Assert.assertEquals(led.getCV().size(), 5);
        //        Assert.assertNotNull(led.getCV().get(0));
        //        Assert.assertEquals(led.getCV().get(0).getNumber(), 0);
        //        Assert.assertEquals(led.getCV().get(0).getType(), DataType.BYTE);
        //        Assert.assertEquals(led.getCV().get(0).getMin(), "-");
        //        // check the servo values
        //        Assert.assertNotNull(vendorCV.getTemplates().getServo());
        //        Assert.assertEquals(vendorCV.getTemplates().getServo().getCV().size(), 12);

        Assert.assertNotNull(vendorCV.getCVDefinition());
        CVDefinitionType cvDefinition = vendorCV.getCVDefinition();
        // basis
        Assert.assertNotNull(cvDefinition.getBasis());
        Basis basis = cvDefinition.getBasis();
        Assert.assertNotNull(basis.getDCC());
        DCC dcc = basis.getDCC();
        Assert.assertNotNull(dcc.getCV());
        Assert.assertEquals(dcc.getCV().size(), 2);
        Assert.assertNotNull(basis.getCV());
        Assert.assertEquals(basis.getCV().size(), 11);
        // servos
        Assert.assertNotNull(cvDefinition.getServos());
        Servos servos = cvDefinition.getServos();

        // leds
        Assert.assertNotNull(cvDefinition.getLEDS());
        LEDS leds = cvDefinition.getLEDS();
    }

    @Test
    public void loadVendorCVForS88BridgeAndTransformToWizardFormatTest() throws JAXBException, TransformerException,
        IOException {
        LOGGER.info("Load VendorCV for S88Bridge and transform to wizard data.");

        System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");

        InputStream xsltStream = VendorCVTest.class.getResourceAsStream("/xsd/vendor_cv.xslt");
        javax.xml.transform.Source xsltSource = new javax.xml.transform.stream.StreamSource(xsltStream);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer(xsltSource);
        //        transformer.setOutputProperty(OutputKeys.INDENT, "no");

        InputStream is = VendorCVTest.class.getResourceAsStream("/xsd/monitor-0.4.2.3/40.00.0D.69.00-VendorCV.xml");
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
        Basis basis = new Basis();
        CV cv = new CV();
        cv.setNumber(1);
        cv.setType(DataType.BYTE);
        cv.setMin("0");
        cv.setMax("255");
        cv.setLow("-");
        cv.setHigh("-");
        cv.setValues("-");
        cv.setMode(ModeType.RO);
        // TODO
        //        cv.setDescde("vendor_id");
        //        cv.setDescen("vendor_id");
        basis.getCV().add(cv);

        cv = new CV();
        cv.setNumber(2);
        cv.setType(DataType.BYTE);
        cv.setMin("0");
        cv.setMax("255");
        cv.setLow("-");
        cv.setHigh("-");
        cv.setValues("-");
        cv.setMode(ModeType.RO);
        // TODO
        //        cv.setDescde("hw_id");
        //        cv.setDescen("hw_id");
        basis.getCV().add(cv);

        cvDefinition.setBasis(basis);
        vendorCV.setCVDefinition(cvDefinition);

        File exportFile = new File(EXPORTED_CVDEF_TARGET_DIR, "vendorcv_S88Bridge.xml");
        OutputStream os = new BufferedOutputStream(new FileOutputStream(exportFile));

        marshaller.marshal(vendorCV, os);

        os.flush();
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

        VendorCV vendorCV = new VendorCV();
        vendorCV.setVersion(versionInfo);

        TemplatesType templatesType = new TemplatesType();
        LED led = new LED();
        CV cv = new CV();
        cv.setNumber(0);
        cv.setType(DataType.BYTE);
        cv.setMode(ModeType.RW);
        // TODO
        //        cv.setDescde("LED: Einstellung der Stromquelle");
        //        cv.setDescen("LED: courrent source setup");
        cv.setMin("-");
        cv.setMax("-");
        cv.setLow("-");
        cv.setHigh("-");
        cv.setValues("-");
        led.getCV().add(cv);
        cv = new CV();
        cv.setNumber(1);
        cv.setType(DataType.BYTE);
        cv.setMode(ModeType.RW);
        // TODO
        //        cv.setDescde("LED: Helligkeit für Zustand „aus“");
        //        cv.setDescen("LED: light intensity at status 'off'");
        led.getCV().add(cv);
        // TODO enable
        templatesType.getTemplate().add(led);

        Servo servo = new Servo();
        cv = new CV();
        cv.setNumber(0);
        cv.setType(DataType.BYTE);
        cv.setMode(ModeType.RW);
        servo.getCV().add(cv);
        cv = new CV();
        cv.setNumber(1);
        cv.setType(DataType.BYTE);
        cv.setMode(ModeType.RW);
        servo.getCV().add(cv);
        templatesType.getTemplate().add(servo);

        vendorCV.setTemplates(templatesType);

        File exportFile = new File(EXPORTED_CVDEF_TARGET_DIR, "vendorcv_0.xml");
        OutputStream os = new BufferedOutputStream(new FileOutputStream(exportFile));

        marshaller.marshal(vendorCV, os);

        os.flush();

    }
}
