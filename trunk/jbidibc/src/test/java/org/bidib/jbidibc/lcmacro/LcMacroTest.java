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

public class LcMacroTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LcMacroTest.class);

    private static final String JAXB_PACKAGE = "org.bidib.jbidibc.lcmacro";

    private static final String XSD_LOCATION = "xsd/macros.xsd";

    @Test
    public void loadMacroTest() throws JAXBException {
        LOGGER.info("Load macro.");

        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        InputStream is = LcMacroTest.class.getResourceAsStream("/xsd/macros.xml");
        LcMacros macros = (LcMacros) unmarshaller.unmarshal(is);
        Assert.assertNotNull(macros);

        Assert.assertNotNull(macros.getLcMacro());
        Assert.assertNotNull(macros.getLcMacro().getLcMacroPoint());

        Assert.assertEquals(macros.getLcMacro().getLcMacroPoint().size(), 4);

        LcMacroPointType lcMacroStep = macros.getLcMacro().getLcMacroPoint().get(1);
        Assert.assertTrue(lcMacroStep instanceof CriticalSectionStep);
        CriticalSectionStep criticalSectionStep = (CriticalSectionStep) lcMacroStep;
        Assert.assertNotNull(criticalSectionStep.getCriticalSectionActionType());
        Assert.assertEquals(criticalSectionStep.getCriticalSectionActionType(), CriticalSectionActionType.BEGIN);

        lcMacroStep = macros.getLcMacro().getLcMacroPoint().get(3);
        Assert.assertTrue(lcMacroStep instanceof CriticalSectionStep);
        criticalSectionStep = (CriticalSectionStep) lcMacroStep;
        Assert.assertNotNull(criticalSectionStep.getCriticalSectionActionType());
        Assert.assertEquals(criticalSectionStep.getCriticalSectionActionType(), CriticalSectionActionType.END);

    }

    @Test
    public void saveMacroTest() throws JAXBException, SAXException, IOException {

        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, XSD_LOCATION);

        LcMacroType lcMacro = new LcMacroType();
        lcMacro.setMacroId(1);
        lcMacro.setMacroName("TestMacro1");
        lcMacro.setRepeat(1);
        lcMacro.setSlowdown(1);

        LightPortStep lightPortStep = new LightPortStep();
        lightPortStep.setStepNumber(0);
        lightPortStep.setDelay(6);
        lightPortStep.setLightPortActionType(LightPortActionType.ON);

        lcMacro.getLcMacroPoint().add(lightPortStep);

        lightPortStep = new LightPortStep();
        lightPortStep.setStepNumber(1);
        lightPortStep.setDelay(6);
        lightPortStep.setLightPortActionType(LightPortActionType.OFF);

        lcMacro.getLcMacroPoint().add(lightPortStep);

        ServoPortStep servoPortStep = new ServoPortStep();
        servoPortStep.setStepNumber(2);
        servoPortStep.setDelay(60);
        ServoPortActionType servoPortActionType = new ServoPortActionType();
        servoPortActionType.setAction(ServoActionType.START);
        servoPortActionType.setDestination(90);
        servoPortStep.setServoPortActionType(servoPortActionType);

        lcMacro.getLcMacroPoint().add(servoPortStep);

        LcMacros lcMacros = new LcMacros();
        lcMacros.setLcMacro(lcMacro);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        marshaller.marshal(lcMacros, baos);

        LOGGER.info("marshalled macro: {}", baos);

        String d = baos.toString("UTF-8");
        Document testDoc = XMLUnit.buildControlDocument(d);

        InputStream is = LcMacroTest.class.getResourceAsStream("/xsd/macro-export-test.xml");
        final String xmlContent = IOUtils.toString(is, "UTF-8");
        Document controlDoc = XMLUnit.buildControlDocument(xmlContent);

        XMLAssert.assertXMLEqual(controlDoc, testDoc);
    }
}
