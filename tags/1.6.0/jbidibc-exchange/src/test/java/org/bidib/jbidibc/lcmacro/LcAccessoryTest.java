package org.bidib.jbidibc.lcmacro;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class LcAccessoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LcAccessoryTest.class);

    private static final String JAXB_PACKAGE = "org.bidib.jbidibc.lcmacro";

    private static final String XSD_LOCATION = "xsd/accessories.xsd";

    @Test
    public void loadAccessoryTest() throws JAXBException {
        LOGGER.info("Load accessory.");

        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        InputStream is = LcMacroTest.class.getResourceAsStream("/xsd/accessories.xml");
        LcAccessories accessories = (LcAccessories) unmarshaller.unmarshal(is);
        Assert.assertNotNull(accessories);

        Assert.assertNotNull(accessories.getLcAccessory());
        LcAccessoryType lcAccessory = accessories.getLcAccessory();
        Assert.assertNotNull(lcAccessory.getLcAspectPoint());
        Assert.assertTrue(lcAccessory.getLcAspectPoint().size() == 4);

        LcAspectPointType lcAspectPoint = lcAccessory.getLcAspectPoint().get(2);
        Assert.assertNotNull(lcAspectPoint);
        Assert.assertEquals(lcAspectPoint.getMacroId(), 3);
        Assert.assertNull(lcAspectPoint.getAspectName());

        lcAspectPoint = lcAccessory.getLcAspectPoint().get(3);
        Assert.assertNotNull(lcAspectPoint);
        Assert.assertEquals(lcAspectPoint.getMacroId(), 4);
        Assert.assertEquals(lcAspectPoint.getAspectName(), "Abzweig");
    }

    @Test
    public void saveAccessoryTest() throws JAXBException, SAXException, IOException {

        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, XSD_LOCATION);

        LcAccessoryType lcAccessory = new LcAccessoryType();
        lcAccessory.setAccessoryId(1);
        lcAccessory.setAccessoryName("Accessory_0");

        LcAspectPointType lcAspectPoint = new LcAspectPointType();
        lcAspectPoint.setAspectName("Gerade");
        lcAspectPoint.setMacroId(1);
        lcAccessory.getLcAspectPoint().add(lcAspectPoint);

        lcAspectPoint = new LcAspectPointType();
        lcAspectPoint.setAspectName("Gerade");
        lcAspectPoint.setMacroId(2);
        lcAccessory.getLcAspectPoint().add(lcAspectPoint);

        lcAspectPoint = new LcAspectPointType();
        // don't set the name as it's optional
        lcAspectPoint.setMacroId(3);
        lcAccessory.getLcAspectPoint().add(lcAspectPoint);

        LcAccessories lcAccessories = new LcAccessories();
        lcAccessories.setLcAccessory(lcAccessory);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        marshaller.marshal(lcAccessories, baos);

        LOGGER.info("marshalled macro: {}", baos);

        String d = baos.toString("UTF-8");
        Document testDoc = XMLUnit.buildControlDocument(d);

        InputStream is = LcMacroTest.class.getResourceAsStream("/xsd/accessories-export-test.xml");
        final String xmlContent = IOUtils.toString(is, "UTF-8");
        Document controlDoc = XMLUnit.buildControlDocument(xmlContent);

        XMLAssert.assertXMLEqual(controlDoc, testDoc);
    }
}
