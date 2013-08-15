package org.bidib.jbidibc.lcmacro;

import org.bidib.jbidibc.LcMacro;
import org.bidib.jbidibc.enumeration.AnalogPortEnum;
import org.bidib.jbidibc.enumeration.BidibEnum;
import org.bidib.jbidibc.enumeration.LightPortEnum;
import org.bidib.jbidibc.enumeration.ServoPortEnum;
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
            case LIGHTPORT:
                LightPortEnum lightPortEnum = LightPortEnum.valueOf(val.getType());
                lcMacroStep.setLightPortActionType(LightPortActionType.fromValue(lightPortEnum.name()));
                break;
            case SERVOPORT:
                ServoPortEnum servoPortEnum = ServoPortEnum.valueOf(val.getType());
                ServoPortActionType servoPortActionType = new ServoPortActionType();
                servoPortActionType.setAction(ServoActionType.fromValue(servoPortEnum.name()));
                servoPortActionType.setDestination(lcMacro.getValue());
                lcMacroStep.setServoPortActionType(servoPortActionType);
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
