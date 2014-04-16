package org.bidib.jbidibc.vendorcv;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
