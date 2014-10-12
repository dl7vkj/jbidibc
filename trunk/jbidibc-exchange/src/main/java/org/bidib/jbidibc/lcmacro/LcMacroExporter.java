package org.bidib.jbidibc.lcmacro;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.bidib.jbidibc.core.LcMacro;
import org.bidib.jbidibc.core.enumeration.AnalogPortEnum;
import org.bidib.jbidibc.core.enumeration.BacklightPortEnum;
import org.bidib.jbidibc.core.enumeration.BidibEnum;
import org.bidib.jbidibc.core.enumeration.LightPortEnum;
import org.bidib.jbidibc.core.enumeration.MotorPortEnum;
import org.bidib.jbidibc.core.enumeration.ServoPortEnum;
import org.bidib.jbidibc.core.enumeration.SoundPortEnum;
import org.bidib.jbidibc.core.enumeration.SwitchPortEnum;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LcMacroExporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(LcMacroExporter.class);

    public static final String JAXB_PACKAGE = "org.bidib.jbidibc.lcmacro";

    public static final String XSD_LOCATION = "/xsd/macros.xsd";

    public LcMacroPointType prepareLcMacroPoint(LcMacro lcMacro) {
        LOGGER.info("Export the LcMacro: {}", lcMacro);

        LcMacroPointType lcMacroPoint = null;

        BidibEnum val = lcMacro.getStatus();
        switch (lcMacro.getOutputType()) {
            case ACCESSORY_OKAY_NF:
                AccessoryOkayPoint accessoryOkayNFPoint = new AccessoryOkayPoint();
                // no input number for no feedback
                accessoryOkayNFPoint.setAccessoryOkayActionType(AccessoryOkayActionType.NO_FEEDBACK);
                accessoryOkayNFPoint.setDelay(ByteUtils.getInt(lcMacro.getDelay()));
                lcMacroPoint = accessoryOkayNFPoint;
                break;
            case ACCESSORY_OKAY_INPUTQUERY0:
                AccessoryOkayPoint accessoryOkay0Point = new AccessoryOkayPoint();
                accessoryOkay0Point.setInputNumber(ByteUtils.getInt(lcMacro.getOutputNumber()));
                accessoryOkay0Point.setAccessoryOkayActionType(AccessoryOkayActionType.QUERY_0);
                accessoryOkay0Point.setDelay(ByteUtils.getInt(lcMacro.getDelay()));
                lcMacroPoint = accessoryOkay0Point;
                break;
            case ACCESSORY_OKAY_INPUTQUERY1:
                AccessoryOkayPoint accessoryOkay1Point = new AccessoryOkayPoint();
                accessoryOkay1Point.setInputNumber(ByteUtils.getInt(lcMacro.getOutputNumber()));
                accessoryOkay1Point.setAccessoryOkayActionType(AccessoryOkayActionType.QUERY_1);
                accessoryOkay1Point.setDelay(ByteUtils.getInt(lcMacro.getDelay()));
                lcMacroPoint = accessoryOkay1Point;
                break;
            case SERVOMOVE_QUERY:
                ServoMoveQueryPoint servoMoveQueryPoint = new ServoMoveQueryPoint();
                servoMoveQueryPoint.setOutputNumber(ByteUtils.getInt(lcMacro.getOutputNumber()));
                lcMacroPoint = servoMoveQueryPoint;
                break;
            case ANALOGPORT:
                AnalogPortEnum analogPortEnum = AnalogPortEnum.valueOf(val.getType());
                AnalogPortPoint analogPortPoint = new AnalogPortPoint();
                AnalogPortActionType analogPortAction = new AnalogPortActionType();
                analogPortAction.setAction(AnalogActionType.fromValue(analogPortEnum.name()));
                analogPortAction.setValue(ByteUtils.getInt(lcMacro.getValue()));
                analogPortPoint.setAnalogPortActionType(analogPortAction);
                analogPortPoint.setOutputNumber(lcMacro.getOutputNumber());
                analogPortPoint.setDelay(ByteUtils.getInt(lcMacro.getDelay()));
                lcMacroPoint = analogPortPoint;
                break;
            case BACKLIGHTPORT:
                BacklightPortEnum backlightPortEnum = BacklightPortEnum.valueOf(val.getType());
                BacklightPortActionType backlightPortActionType = new BacklightPortActionType();
                backlightPortActionType.setAction(BacklightActionType.fromValue(backlightPortEnum.name()));
                backlightPortActionType.setBrightness(ByteUtils.getInt(lcMacro.getValue()));
                BacklightPortPoint backlightPortPoint = new BacklightPortPoint();
                backlightPortPoint.setBacklightPortActionType(backlightPortActionType);
                backlightPortPoint.setOutputNumber(lcMacro.getOutputNumber());
                backlightPortPoint.setDelay(ByteUtils.getInt(lcMacro.getDelay()));
                lcMacroPoint = backlightPortPoint;
                break;
            case BEGIN_CRITICAL:
                CriticalSectionPoint beginCriticalSectionPoint = new CriticalSectionPoint();
                beginCriticalSectionPoint.setCriticalSectionActionType(CriticalSectionActionType.BEGIN);
                lcMacroPoint = beginCriticalSectionPoint;
                break;
            case DELAY:
                DelayPoint delayPoint = new DelayPoint();
                delayPoint.setDelayActionType(ByteUtils.getInt(lcMacro.getOutputNumber()));
                lcMacroPoint = delayPoint;
                break;
            case END_CRITICAL:
                CriticalSectionPoint endCriticalSectionPoint = new CriticalSectionPoint();
                endCriticalSectionPoint.setCriticalSectionActionType(CriticalSectionActionType.END);
                lcMacroPoint = endCriticalSectionPoint;
                break;
            case FLAG_CLEAR:
                FlagActionType flagClearActionType = new FlagActionType();
                flagClearActionType.setOperation(FlagOperationType.CLEAR);
                flagClearActionType.setFlagNumber(ByteUtils.getInt(lcMacro.getOutputNumber()));
                FlagPoint flagClearPoint = new FlagPoint();
                flagClearPoint.setFlagActionType(flagClearActionType);
                lcMacroPoint = flagClearPoint;
                break;
            case FLAG_SET:
                FlagActionType flagSetActionType = new FlagActionType();
                flagSetActionType.setOperation(FlagOperationType.SET);
                flagSetActionType.setFlagNumber(ByteUtils.getInt(lcMacro.getOutputNumber()));
                FlagPoint flagSetPoint = new FlagPoint();
                flagSetPoint.setFlagActionType(flagSetActionType);
                lcMacroPoint = flagSetPoint;
                break;
            case FLAG_QUERY:
                FlagActionType flagQueryActionType = new FlagActionType();
                flagQueryActionType.setOperation(FlagOperationType.QUERY);
                flagQueryActionType.setFlagNumber(ByteUtils.getInt(lcMacro.getOutputNumber()));
                FlagPoint flagQueryPoint = new FlagPoint();
                flagQueryPoint.setFlagActionType(flagQueryActionType);
                lcMacroPoint = flagQueryPoint;
                break;
            case INPUT_QUERY0:
                InputPortPoint inputQuery0Point = new InputPortPoint();
                inputQuery0Point.setInputNumber(lcMacro.getOutputNumber());
                inputQuery0Point.setInputPortActionType(InputPortActionType.QUERY_0);
                inputQuery0Point.setDelay(ByteUtils.getInt(lcMacro.getDelay()));
                lcMacroPoint = inputQuery0Point;
                break;
            case INPUT_QUERY1:
                InputPortPoint inputQuery1Point = new InputPortPoint();
                inputQuery1Point.setInputNumber(lcMacro.getOutputNumber());
                inputQuery1Point.setInputPortActionType(InputPortActionType.QUERY_1);
                inputQuery1Point.setDelay(ByteUtils.getInt(lcMacro.getDelay()));
                lcMacroPoint = inputQuery1Point;
                break;
            case LIGHTPORT:
                LightPortEnum lightPortEnum = LightPortEnum.valueOf(val.getType());
                LightPortPoint lightPortPoint = new LightPortPoint();
                lightPortPoint.setLightPortActionType(LightPortActionType.fromValue(lightPortEnum.name()));
                lightPortPoint.setOutputNumber(lcMacro.getOutputNumber());
                lightPortPoint.setDelay(ByteUtils.getInt(lcMacro.getDelay()));
                lcMacroPoint = lightPortPoint;
                break;
            case END_OF_MACRO:
                MacroActionType macroEndActionType = new MacroActionType();
                macroEndActionType.setOperation(MacroOperationType.END);
                macroEndActionType.setMacroNumber(ByteUtils.getInt(lcMacro.getOutputNumber()));
                MacroActionPoint macroEndActionPoint = new MacroActionPoint();
                macroEndActionPoint.setMacroActionType(macroEndActionType);
                lcMacroPoint = macroEndActionPoint;
                break;
            case START_MACRO:
                MacroActionType macroStartActionType = new MacroActionType();
                macroStartActionType.setOperation(MacroOperationType.START);
                macroStartActionType.setMacroNumber(ByteUtils.getInt(lcMacro.getOutputNumber()));
                MacroActionPoint macroStartActionPoint = new MacroActionPoint();
                macroStartActionPoint.setMacroActionType(macroStartActionType);
                lcMacroPoint = macroStartActionPoint;
                break;
            case STOP_MACRO:
                MacroActionType macroStopActionType = new MacroActionType();
                macroStopActionType.setOperation(MacroOperationType.STOP);
                macroStopActionType.setMacroNumber(ByteUtils.getInt(lcMacro.getOutputNumber()));
                MacroActionPoint macroStopActionPoint = new MacroActionPoint();
                macroStopActionPoint.setMacroActionType(macroStopActionType);
                lcMacroPoint = macroStopActionPoint;
                break;
            case MOTORPORT:
                MotorPortEnum motorPortEnum = MotorPortEnum.valueOf(val.getType());
                MotorPortActionType motorPortActionType = new MotorPortActionType();
                motorPortActionType.setAction(MotorActionType.fromValue(motorPortEnum.name()));
                motorPortActionType.setValue(ByteUtils.getInt(lcMacro.getValue()));
                MotorPortPoint motorPortPoint = new MotorPortPoint();
                motorPortPoint.setMotorPortActionType(motorPortActionType);
                motorPortPoint.setOutputNumber(lcMacro.getOutputNumber());
                motorPortPoint.setDelay(ByteUtils.getInt(lcMacro.getDelay()));
                lcMacroPoint = motorPortPoint;
                break;
            case RANDOM_DELAY:
                RandomDelayPoint randomDelayPoint = new RandomDelayPoint();
                randomDelayPoint.setRandomDelayActionType(ByteUtils.getInt(lcMacro.getOutputNumber()));
                lcMacroPoint = randomDelayPoint;
                break;
            case SERVOPORT:
                ServoPortEnum servoPortEnum = ServoPortEnum.valueOf(val.getType());
                ServoPortActionType servoPortActionType = new ServoPortActionType();
                servoPortActionType.setAction(ServoActionType.fromValue(servoPortEnum.name()));
                servoPortActionType.setDestination(ByteUtils.getInt(lcMacro.getValue()));
                ServoPortPoint servoPortPoint = new ServoPortPoint();
                servoPortPoint.setServoPortActionType(servoPortActionType);
                servoPortPoint.setOutputNumber(lcMacro.getOutputNumber());
                servoPortPoint.setDelay(ByteUtils.getInt(lcMacro.getDelay()));
                lcMacroPoint = servoPortPoint;
                break;
            case SOUNDPORT:
                SoundPortEnum soundPortEnum = SoundPortEnum.valueOf(val.getType());
                SoundPortActionType soundPortActionType = new SoundPortActionType();
                soundPortActionType.setAction(SoundActionType.fromValue(soundPortEnum.name()));
                soundPortActionType.setValue(ByteUtils.getInt(lcMacro.getValue()));
                SoundPortPoint soundPortPoint = new SoundPortPoint();
                soundPortPoint.setSoundPortActionType(soundPortActionType);
                soundPortPoint.setOutputNumber(lcMacro.getOutputNumber());
                soundPortPoint.setDelay(ByteUtils.getInt(lcMacro.getDelay()));
                lcMacroPoint = soundPortPoint;
                break;
            case SWITCHPORT:
                SwitchPortEnum switchPortEnum = SwitchPortEnum.valueOf(val.getType());
                SwitchPortPoint switchPortPoint = new SwitchPortPoint();
                switchPortPoint.setSwitchPortActionType(SwitchPortActionType.fromValue(switchPortEnum.name()));
                switchPortPoint.setOutputNumber(lcMacro.getOutputNumber());
                switchPortPoint.setDelay(ByteUtils.getInt(lcMacro.getDelay()));
                lcMacroPoint = switchPortPoint;
                break;
            default:
                LOGGER.warn("Unsupported port type detected!");
                lcMacroPoint = null;
                break;
        }
        // lcMacroPoint.setIndex(ByteUtils.getInt(lcMacro.getStepNumber()));

        LOGGER.info("Return lcMacroPoint: {}", lcMacroPoint);
        return lcMacroPoint;
    }

    /**
     * Save a single macro
     * 
     * @param lcMacro
     *            the macro
     * @param fileName
     *            the filename
     */
    public void saveMacro(LcMacroType lcMacro, String fileName, boolean gzip) {
        LOGGER.info("Save macro content to file: {}, lcMacro: {}", fileName, lcMacro);

        OutputStream os = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            // marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, XSD_LOCATION);
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            StreamSource streamSource = new StreamSource(LcMacroExporter.class.getResourceAsStream(XSD_LOCATION));
            Schema schema = schemaFactory.newSchema(streamSource);
            marshaller.setSchema(schema);

            LcMacros lcMacros = new LcMacros();
            lcMacros.setLcMacro(lcMacro);

            os = new BufferedOutputStream(new FileOutputStream(fileName));
            if (gzip) {
                LOGGER.debug("Use gzip to compress macro.");
                os = new GZIPOutputStream(os);
            }

            marshaller.marshal(lcMacros, os);

            os.flush();

            LOGGER.info("Save macro content to file passed: {}", fileName);
        }
        catch (Exception ex) {
            // TODO add better exception handling
            LOGGER.warn("Save macro failed.", ex);

            throw new RuntimeException("Save macro failed.", ex);
        }
        finally {
            if (os != null) {
                try {
                    os.close();
                }
                catch (IOException ex) {
                    LOGGER.warn("Close outputstream failed.", ex);
                }
            }
        }
    }

    public LcMacroType loadMacro(String fileName) {
        LOGGER.info("Load macro content from file: {}", fileName);

        InputStream is = null;
        LcMacros macros = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            StreamSource streamSource = new StreamSource(LcMacroExporter.class.getResourceAsStream(XSD_LOCATION));
            Schema schema = schemaFactory.newSchema(streamSource);
            unmarshaller.setSchema(schema);

            File importFile = new File(fileName);
            is = new FileInputStream(importFile);

            macros = (LcMacros) unmarshaller.unmarshal(is);
            LOGGER.info("Loaded macros from file: {}", fileName);
        }
        catch (Exception ex) {
            // TODO add better exception handling
            LOGGER.warn("Load macro failed.", ex);

            throw new RuntimeException("Load macro failed.", ex);
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (IOException ex) {
                    LOGGER.warn("Close inputstream failed.", ex);
                }
            }
        }

        LcMacroType lcMacro = null;
        if (macros != null) {
            lcMacro = macros.getLcMacro();
            LOGGER.debug("Loaded macro: {}", lcMacro);
        }

        return lcMacro;
    }
}
