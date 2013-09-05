package org.bidib.jbidibc.vendorcv;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

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
    public void loadVendorCVTest() throws JAXBException {
        LOGGER.info("Load VendorCV.");

        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        InputStream is = VendorCVTest.class.getResourceAsStream("/xsd/05.00.0D.6B.00-VendorCV.xml");
        VendorCV vendorCV = (VendorCV) unmarshaller.unmarshal(is);
        Assert.assertNotNull(vendorCV);

        Assert.assertNotNull(vendorCV.getVersion());
        VersionInfoType versionInfo = vendorCV.getVersion();
        Assert.assertEquals(versionInfo.getVersion(), "0.1");
        Assert.assertEquals(versionInfo.getVendor(), "013");

        Assert.assertNotNull(vendorCV.getTemplates());
        Assert.assertNotNull(vendorCV.getTemplates().getLED());
        Assert.assertEquals(vendorCV.getTemplates().getLED().getCV().size(), 5);
        Assert.assertNotNull(vendorCV.getTemplates().getServo());
        Assert.assertEquals(vendorCV.getTemplates().getServo().getCV().size(), 12);
    }

    @Test
    public void saveVendorCVTest() throws JAXBException, SAXException, IOException {

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
        cv.setDescde("LED: Einstellung der Stromquelle");
        cv.setDescen("LED: courrent source setup");
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
        cv.setDescde("LED: Helligkeit für Zustand „aus“");
        cv.setDescen("LED: light intensity at status 'off'");
        led.getCV().add(cv);
        templatesType.setLED(led);

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
        templatesType.setServo(servo);

        vendorCV.setTemplates(templatesType);

        File exportFile = new File(EXPORTED_CVDEF_TARGET_DIR, "vendorcv_0.xml");
        OutputStream os = new BufferedOutputStream(new FileOutputStream(exportFile));

        marshaller.marshal(vendorCV, os);

        os.flush();

    }
}
