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

    public LcMacroPointType prepareLcMacroPoint(LcMacro lcMacro) {
        LOGGER.info("Export the LcMacro: {}", lcMacro);

        LcMacroPointType lcMacroStep = null;

        BidibEnum val = lcMacro.getStatus();
        switch (lcMacro.getOutputType()) {
            case ANALOGPORT:
                AnalogPortEnum analogPortEnum = AnalogPortEnum.valueOf(val.getType());
                AnalogPortStep analogPortStep = new AnalogPortStep();
                analogPortStep.setAnalogPortActionType(AnalogPortActionType.fromValue(analogPortEnum.name()));
                lcMacroStep = analogPortStep;
                break;
            case BEGIN_CRITICAL:
                CriticalSectionStep beginCriticalSectionStep = new CriticalSectionStep();
                beginCriticalSectionStep.setCriticalSectionActionType(CriticalSectionActionType.BEGIN);
                lcMacroStep = beginCriticalSectionStep;
                break;
            case DELAY:
                DelayStep delayStep = new DelayStep();
                delayStep.setDelayActionType(ByteUtils.getInt(lcMacro.getOutputNumber()));
                lcMacroStep = delayStep;
                break;
            case END_CRITICAL:
                CriticalSectionStep endCriticalSectionStep = new CriticalSectionStep();
                endCriticalSectionStep.setCriticalSectionActionType(CriticalSectionActionType.END);
                lcMacroStep = endCriticalSectionStep;
                break;
            case FLAG_CLEAR:
                FlagActionType flagClearActionType = new FlagActionType();
                flagClearActionType.setOperation(FlagOperationType.CLEAR);
                flagClearActionType.setPortNumber(ByteUtils.getInt(lcMacro.getOutputNumber()));
                FlagStep flagClearStep = new FlagStep();
                flagClearStep.setFlagActionType(flagClearActionType);
                lcMacroStep = flagClearStep;
                break;
            case FLAG_SET:
                FlagActionType flagSetActionType = new FlagActionType();
                flagSetActionType.setOperation(FlagOperationType.SET);
                flagSetActionType.setPortNumber(ByteUtils.getInt(lcMacro.getOutputNumber()));
                FlagStep flagSetStep = new FlagStep();
                flagSetStep.setFlagActionType(flagSetActionType);
                lcMacroStep = flagSetStep;
                break;
            case FLAG_QUERY:
                FlagActionType flagQueryActionType = new FlagActionType();
                flagQueryActionType.setOperation(FlagOperationType.QUERY);
                flagQueryActionType.setPortNumber(ByteUtils.getInt(lcMacro.getOutputNumber()));
                FlagStep flagQueryStep = new FlagStep();
                flagQueryStep.setFlagActionType(flagQueryActionType);
                lcMacroStep = flagQueryStep;
                break;
            case INPUT_QUERY0:
                InputQuery0Step inputQuery0Step = new InputQuery0Step();
                inputQuery0Step.setInputQuery0ActionType(ByteUtils.getInt(lcMacro.getOutputNumber()));
                lcMacroStep = inputQuery0Step;
                break;
            case INPUT_QUERY1:
                InputQuery1Step inputQuery1Step = new InputQuery1Step();
                inputQuery1Step.setInputQuery1ActionType(ByteUtils.getInt(lcMacro.getOutputNumber()));
                lcMacroStep = inputQuery1Step;
                break;
            case LIGHTPORT:
                LightPortEnum lightPortEnum = LightPortEnum.valueOf(val.getType());
                LightPortStep lightPortStep = new LightPortStep();
                lightPortStep.setLightPortActionType(LightPortActionType.fromValue(lightPortEnum.name()));
                lcMacroStep = lightPortStep;
                break;
            case END_OF_MACRO:
                MacroActionType macroEndActionType = new MacroActionType();
                macroEndActionType.setOperation(MacroOperationType.END);
                macroEndActionType.setPortNumber(ByteUtils.getInt(lcMacro.getOutputNumber()));
                MacroActionStep macroEndActionStep = new MacroActionStep();
                macroEndActionStep.setMacroActionType(macroEndActionType);
                lcMacroStep = macroEndActionStep;
                break;
            case START_MACRO:
                MacroActionType macroStartActionType = new MacroActionType();
                macroStartActionType.setOperation(MacroOperationType.START);
                macroStartActionType.setPortNumber(ByteUtils.getInt(lcMacro.getOutputNumber()));
                MacroActionStep macroStartActionStep = new MacroActionStep();
                macroStartActionStep.setMacroActionType(macroStartActionType);
                lcMacroStep = macroStartActionStep;
                break;
            case STOP_MACRO:
                MacroActionType macroStopActionType = new MacroActionType();
                macroStopActionType.setOperation(MacroOperationType.STOP);
                macroStopActionType.setPortNumber(ByteUtils.getInt(lcMacro.getOutputNumber()));
                MacroActionStep macroStopActionStep = new MacroActionStep();
                macroStopActionStep.setMacroActionType(macroStopActionType);
                lcMacroStep = macroStopActionStep;
                break;
            case MOTORPORT:
                MotorPortEnum motorPortEnum = MotorPortEnum.valueOf(val.getType());
                MotorPortStep motorPortStep = new MotorPortStep();
                motorPortStep.setMotorPortActionType(MotorPortActionType.fromValue(motorPortEnum.name()));
                lcMacroStep = motorPortStep;
                break;
            case RANDOM_DELAY:
                RandomDelayStep randomDelayStep = new RandomDelayStep();
                randomDelayStep.setRandomDelayActionType(ByteUtils.getInt(lcMacro.getOutputNumber()));
                lcMacroStep = randomDelayStep;
                break;
            case SERVOPORT:
                ServoPortEnum servoPortEnum = ServoPortEnum.valueOf(val.getType());
                ServoPortActionType servoPortActionType = new ServoPortActionType();
                servoPortActionType.setAction(ServoActionType.fromValue(servoPortEnum.name()));
                servoPortActionType.setDestination(lcMacro.getValue());
                ServoPortStep servoPortStep = new ServoPortStep();
                servoPortStep.setServoPortActionType(servoPortActionType);
                lcMacroStep = servoPortStep;
                break;
            case SOUNDPORT:
                SoundPortEnum soundPortEnum = SoundPortEnum.valueOf(val.getType());
                SoundPortStep soundPortStep = new SoundPortStep();
                soundPortStep.setSoundPortActionType(SoundPortActionType.fromValue(soundPortEnum.name()));
                lcMacroStep = soundPortStep;
                break;
            case SWITCHPORT:
                SwitchPortEnum switchPortEnum = SwitchPortEnum.valueOf(val.getType());
                SwitchPortStep switchPortStep = new SwitchPortStep();
                switchPortStep.setSwitchPortActionType(SwitchPortActionType.fromValue(switchPortEnum.name()));
                lcMacroStep = switchPortStep;
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
