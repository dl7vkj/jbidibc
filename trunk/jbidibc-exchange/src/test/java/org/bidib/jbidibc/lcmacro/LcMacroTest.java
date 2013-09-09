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

        Assert.assertEquals(macros.getLcMacro().getLcMacroPoint().size(), 6);

        LcMacroPointType lcMacroPoint = macros.getLcMacro().getLcMacroPoint().get(1);
        Assert.assertTrue(lcMacroPoint instanceof CriticalSectionPoint);
        CriticalSectionPoint criticalSectionPoint = (CriticalSectionPoint) lcMacroPoint;
        Assert.assertNotNull(criticalSectionPoint.getCriticalSectionActionType());
        Assert.assertEquals(criticalSectionPoint.getCriticalSectionActionType(), CriticalSectionActionType.BEGIN);

        lcMacroPoint = macros.getLcMacro().getLcMacroPoint().get(2);
        Assert.assertTrue(lcMacroPoint instanceof ServoPortPoint);
        ServoPortPoint servoPortPoint = (ServoPortPoint) lcMacroPoint;
        Assert.assertNotNull(servoPortPoint.getServoPortActionType());
        Assert.assertNotNull(servoPortPoint.getServoPortActionType().getAction());
        Assert.assertEquals(servoPortPoint.getServoPortActionType().getAction(), ServoActionType.START);
        Assert.assertEquals(servoPortPoint.getServoPortActionType().getDestination(), 253);
        Assert.assertEquals(servoPortPoint.getOutputNumber(), 1);
        Assert.assertEquals(servoPortPoint.getDelay(), Integer.valueOf(6));

        lcMacroPoint = macros.getLcMacro().getLcMacroPoint().get(3);
        Assert.assertTrue(lcMacroPoint instanceof CriticalSectionPoint);
        criticalSectionPoint = (CriticalSectionPoint) lcMacroPoint;
        Assert.assertNotNull(criticalSectionPoint.getCriticalSectionActionType());
        Assert.assertEquals(criticalSectionPoint.getCriticalSectionActionType(), CriticalSectionActionType.END);

        lcMacroPoint = macros.getLcMacro().getLcMacroPoint().get(4);
        Assert.assertTrue(lcMacroPoint instanceof AnalogPortPoint);
        AnalogPortPoint analogPortPoint = (AnalogPortPoint) lcMacroPoint;
        Assert.assertNotNull(analogPortPoint.getAnalogPortActionType());
        Assert.assertNotNull(analogPortPoint.getAnalogPortActionType().getAction());
        Assert.assertEquals(analogPortPoint.getAnalogPortActionType().getAction(), AnalogActionType.START);
        Assert.assertEquals(analogPortPoint.getAnalogPortActionType().getValue(), 254);
        Assert.assertEquals(analogPortPoint.getOutputNumber(), 4);
        Assert.assertEquals(analogPortPoint.getDelay(), Integer.valueOf(30));

        lcMacroPoint = macros.getLcMacro().getLcMacroPoint().get(5);
        Assert.assertTrue(lcMacroPoint instanceof FlagPoint);
        FlagPoint flagPoint = (FlagPoint) lcMacroPoint;
        Assert.assertNotNull(flagPoint.getFlagActionType());
        Assert.assertEquals(flagPoint.getFlagActionType().getOperation(), FlagOperationType.SET);
        Assert.assertEquals(flagPoint.getFlagActionType().getFlagNumber(), 12);
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

        StartClkType startClkType = new StartClkType();
        startClkType.setHour((byte) 23);
        startClkType.setMinute((byte) 59);
        startClkType.setWeekday(RepeatWeekdayType.TUESDAY);
        lcMacro.setStartClk(startClkType);

        LightPortPoint lightPortPoint = new LightPortPoint();
        //        lightPortPoint.setIndex(0);
        lightPortPoint.setDelay(6);
        lightPortPoint.setOutputNumber(0);
        lightPortPoint.setLightPortActionType(LightPortActionType.ON);

        lcMacro.getLcMacroPoint().add(lightPortPoint);

        lightPortPoint = new LightPortPoint();
        //        lightPortPoint.setIndex(1);
        lightPortPoint.setDelay(6);
        lightPortPoint.setOutputNumber(0);
        lightPortPoint.setLightPortActionType(LightPortActionType.OFF);

        lcMacro.getLcMacroPoint().add(lightPortPoint);

        ServoPortPoint servoPortPoint = new ServoPortPoint();
        //        servoPortPoint.setIndex(2);
        servoPortPoint.setDelay(60);
        servoPortPoint.setOutputNumber(0);
        ServoPortActionType servoPortActionType = new ServoPortActionType();
        servoPortActionType.setAction(ServoActionType.START);
        servoPortActionType.setDestination(230);
        servoPortPoint.setServoPortActionType(servoPortActionType);

        lcMacro.getLcMacroPoint().add(servoPortPoint);

        AnalogPortPoint analogPortPoint = new AnalogPortPoint();
        //        analogPortPoint.setIndex(3);
        analogPortPoint.setDelay(30);
        analogPortPoint.setOutputNumber(0);
        AnalogPortActionType analogPortAction = new AnalogPortActionType();
        analogPortAction.setAction(AnalogActionType.START);
        analogPortAction.setValue(250);
        analogPortPoint.setAnalogPortActionType(analogPortAction);

        lcMacro.getLcMacroPoint().add(analogPortPoint);

        FlagPoint flagPoint = new FlagPoint();
        //        flagPoint.setIndex(4);
        FlagActionType flagActionType = new FlagActionType();
        flagActionType.setFlagNumber(12);
        flagActionType.setOperation(FlagOperationType.SET);
        flagPoint.setFlagActionType(flagActionType);

        lcMacro.getLcMacroPoint().add(flagPoint);

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
