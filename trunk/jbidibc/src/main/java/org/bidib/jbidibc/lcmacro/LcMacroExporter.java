package org.bidib.jbidibc.lcmacro;

import org.bidib.jbidibc.LcMacro;
import org.bidib.jbidibc.enumeration.AnalogPortEnum;
import org.bidib.jbidibc.enumeration.BidibEnum;
import org.bidib.jbidibc.enumeration.LightPortEnum;
import org.bidib.jbidibc.enumeration.MotorPortEnum;
import org.bidib.jbidibc.enumeration.ServoPortEnum;
import org.bidib.jbidibc.enumeration.SoundPortEnum;
import org.bidib.jbidibc.enumeration.SwitchPortEnum;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LcMacroExporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(LcMacroExporter.class);

    public LcMacroStepType prepareLcMacroStep(LcMacro lcMacro) {
        LOGGER.info("Export the LcMacro: {}", lcMacro);
        LcMacroStepType lcMacroStep = new LcMacroStepType();

        BidibEnum val = lcMacro.getStatus();
        switch (lcMacro.getOutputType()) {
            case ANALOGPORT:
                AnalogPortEnum analogPortEnum = AnalogPortEnum.valueOf(val.getType());
                lcMacroStep.setAnalogPortActionType(AnalogPortActionType.fromValue(analogPortEnum.name()));
                break;
            case BEGIN_CRITICAL:
                lcMacroStep.setCriticalSectionActionType(CriticalSectionActionType.BEGIN);
                break;
            case DELAY:
                lcMacroStep.setDelayActionType(ByteUtils.getInt(lcMacro.getOutputNumber()));
                break;
            case END_CRITICAL:
                lcMacroStep.setCriticalSectionActionType(CriticalSectionActionType.END);
                break;
            case FLAG_CLEAR:
                FlagActionType flagClearActionType = new FlagActionType();
                flagClearActionType.setOperation(FlagOperationType.CLEAR);
                flagClearActionType.setPortNumber(ByteUtils.getInt(lcMacro.getOutputNumber()));
                lcMacroStep.setFlagActionType(flagClearActionType);
                break;
            case FLAG_SET:
                FlagActionType flagSetActionType = new FlagActionType();
                flagSetActionType.setOperation(FlagOperationType.SET);
                flagSetActionType.setPortNumber(ByteUtils.getInt(lcMacro.getOutputNumber()));
                lcMacroStep.setFlagActionType(flagSetActionType);
                break;
            case FLAG_QUERY:
                FlagActionType flagQueryActionType = new FlagActionType();
                flagQueryActionType.setOperation(FlagOperationType.QUERY);
                flagQueryActionType.setPortNumber(ByteUtils.getInt(lcMacro.getOutputNumber()));
                lcMacroStep.setFlagActionType(flagQueryActionType);
                break;
            case INPUT_QUERY0:
                lcMacroStep.setInputQuery0ActionType(ByteUtils.getInt(lcMacro.getOutputNumber()));
                break;
            case INPUT_QUERY1:
                lcMacroStep.setInputQuery1ActionType(ByteUtils.getInt(lcMacro.getOutputNumber()));
                break;
            case LIGHTPORT:
                LightPortEnum lightPortEnum = LightPortEnum.valueOf(val.getType());
                lcMacroStep.setLightPortActionType(LightPortActionType.fromValue(lightPortEnum.name()));
                break;
            case END_OF_MACRO:
                MacroActionType macroEndActionType = new MacroActionType();
                macroEndActionType.setOperation(MacroOperationType.END);
                macroEndActionType.setPortNumber(ByteUtils.getInt(lcMacro.getOutputNumber()));
                lcMacroStep.setMacroActionType(macroEndActionType);
                break;
            case START_MACRO:
                MacroActionType macroStartActionType = new MacroActionType();
                macroStartActionType.setOperation(MacroOperationType.START);
                macroStartActionType.setPortNumber(ByteUtils.getInt(lcMacro.getOutputNumber()));
                lcMacroStep.setMacroActionType(macroStartActionType);
                break;
            case STOP_MACRO:
                MacroActionType macroStopActionType = new MacroActionType();
                macroStopActionType.setOperation(MacroOperationType.STOP);
                macroStopActionType.setPortNumber(ByteUtils.getInt(lcMacro.getOutputNumber()));
                lcMacroStep.setMacroActionType(macroStopActionType);
                break;
            case MOTORPORT:
                MotorPortEnum motorPortEnum = MotorPortEnum.valueOf(val.getType());
                lcMacroStep.setMotorPortActionType(MotorPortActionType.fromValue(motorPortEnum.name()));
                break;
            case RANDOM_DELAY:
                lcMacroStep.setRandomDelayActionType(ByteUtils.getInt(lcMacro.getOutputNumber()));
                break;
            case SERVOPORT:
                ServoPortEnum servoPortEnum = ServoPortEnum.valueOf(val.getType());
                ServoPortActionType servoPortActionType = new ServoPortActionType();
                servoPortActionType.setAction(ServoActionType.fromValue(servoPortEnum.name()));
                servoPortActionType.setDestination(lcMacro.getValue());
                lcMacroStep.setServoPortActionType(servoPortActionType);
                break;
            case SOUNDPORT:
                SoundPortEnum soundPortEnum = SoundPortEnum.valueOf(val.getType());
                lcMacroStep.setSoundPortActionType(SoundPortActionType.fromValue(soundPortEnum.name()));
                break;
            case SWITCHPORT:
                SwitchPortEnum switchPortEnum = SwitchPortEnum.valueOf(val.getType());
                lcMacroStep.setSwitchPortActionType(SwitchPortActionType.fromValue(switchPortEnum.name()));
                break;
            default:
                LOGGER.warn("Unsupported port type detected!");
                lcMacroStep = null;
                break;
        }
        lcMacroStep.setDelay(ByteUtils.getInt(lcMacro.getDelay()));
        lcMacroStep.setStepNumber(ByteUtils.getInt(lcMacro.getStepNumber()));

        LOGGER.info("Return lcMacroStep: {}", lcMacroStep);
        return lcMacroStep;
    }

}
