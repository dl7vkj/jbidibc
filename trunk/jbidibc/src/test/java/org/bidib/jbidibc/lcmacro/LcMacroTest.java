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

    @Test
    public void loadMacroTest() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        InputStream is = LcMacroTest.class.getResourceAsStream("/xsd/macros.xml");
        LcMacros macros = (LcMacros) unmarshaller.unmarshal(is);
        Assert.assertNotNull(macros);

        Assert.assertNotNull(macros.getLcMacro());
        Assert.assertNotNull(macros.getLcMacro().getLcMacroStep());

        Assert.assertEquals(macros.getLcMacro().getLcMacroStep().size(), 3);
    }

    @Test
    public void saveMacroTest() throws JAXBException, SAXException, IOException {

        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        LcMacroType lcMacro = new LcMacroType();
        lcMacro.setMacroId(1);
        lcMacro.setMacroName("TestMacro1");
        lcMacro.setRepeat(1);
        lcMacro.setSlowdown(1);

        LcMacroStepType lcMacroStep = new LcMacroStepType();
        lcMacroStep.setStepNumber(0);
        lcMacroStep.setDelay(6);
        lcMacroStep.setLightPortActionType(LightPortActionType.ON);

        lcMacro.getLcMacroStep().add(lcMacroStep);

        lcMacroStep = new LcMacroStepType();
        lcMacroStep.setStepNumber(1);
        lcMacroStep.setDelay(6);
        lcMacroStep.setLightPortActionType(LightPortActionType.OFF);

        lcMacro.getLcMacroStep().add(lcMacroStep);

        lcMacroStep = new LcMacroStepType();
        lcMacroStep.setStepNumber(2);
        lcMacroStep.setDelay(60);
        ServoPortActionType servoPortActionType = new ServoPortActionType();
        servoPortActionType.setAction(ServoActionType.START);
        servoPortActionType.setDestination(90);
        lcMacroStep.setServoPortActionType(servoPortActionType);

        lcMacro.getLcMacroStep().add(lcMacroStep);

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
