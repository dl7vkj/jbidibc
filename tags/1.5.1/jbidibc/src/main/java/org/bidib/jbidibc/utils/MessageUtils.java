package org.bidib.jbidibc.utils;

import org.bidib.jbidibc.LcMacro;
import org.bidib.jbidibc.enumeration.AccessoryOkayEnum;
import org.bidib.jbidibc.enumeration.AnalogPortEnum;
import org.bidib.jbidibc.enumeration.BacklightPortEnum;
import org.bidib.jbidibc.enumeration.BidibEnum;
import org.bidib.jbidibc.enumeration.FlagEnum;
import org.bidib.jbidibc.enumeration.LcOutputType;
import org.bidib.jbidibc.enumeration.LightPortEnum;
import org.bidib.jbidibc.enumeration.MotorPortEnum;
import org.bidib.jbidibc.enumeration.ServoPortEnum;
import org.bidib.jbidibc.enumeration.SoundPortEnum;
import org.bidib.jbidibc.enumeration.SwitchPortEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageUtils.class);

    /**
     * Convert the value of an output type to the corresponding BidibEnum.
     * @param outputType the output type
     * @param value the value.
     * @return the BidibEnum value
     */
    public static BidibEnum toPortStatus(LcOutputType outputType, byte value) {
        BidibEnum result = null;

        if (outputType == LcOutputType.ANALOGPORT) {
            result = AnalogPortEnum.valueOf(value);
        }
        else if (outputType == LcOutputType.BACKLIGHTPORT) {
            // the pseudo status is always start, because the value is delivered
            result = BacklightPortEnum.START;
        }
        else if (outputType == LcOutputType.LIGHTPORT) {
            result = LightPortEnum.valueOf(value);
        }
        else if (outputType == LcOutputType.MOTORPORT) {
            result = MotorPortEnum.valueOf(value);
        }
        else if (outputType == LcOutputType.SERVOPORT) {
            // the pseudo status is always start, because the value is delivered
            result = ServoPortEnum.START;
        }
        else if (outputType == LcOutputType.SOUNDPORT) {
            result = SoundPortEnum.valueOf(value);
        }
        else if (outputType == LcOutputType.SWITCHPORT) {
            result = SwitchPortEnum.valueOf(value);
        }
        else if (outputType != null && outputType.hasPortStatus()) {
            LOGGER.warn("No port status available for output type: {}", outputType);
            throw new RuntimeException("No port status available for output type: " + outputType);
        }

        return result;
    }

    /**
     * Get the port status value from the provided macro step.
     * @param macro the macro step
     * @return the port status
     */
    public static byte getPortStatus(LcMacro macro) {
        byte result = 0;
        BidibEnum status = macro.getStatus();

        if (status != null) {
            if (status instanceof AnalogPortEnum) {
                result = ((AnalogPortEnum) status).getType();
            }
            else if (status instanceof BacklightPortEnum) {
                result = macro.getValue();
            }
            else if (status instanceof FlagEnum) {
                result = ((FlagEnum) status).getType();
            }
            else if (status instanceof LightPortEnum) {
                result = ((LightPortEnum) status).getType();
            }
            else if (status instanceof MotorPortEnum) {
                result = ((MotorPortEnum) status).getType();
            }
            else if (status instanceof ServoPortEnum) {
                result = macro.getValue();
            }
            else if (status instanceof SoundPortEnum) {
                result = ((SoundPortEnum) status).getType();
            }
            else if (status instanceof SwitchPortEnum) {
                result = ((SwitchPortEnum) status).getType();
            }
            else if (status instanceof AccessoryOkayEnum) {
                result = ((AccessoryOkayEnum) status).getType();
            }
            else {
                LOGGER.warn("Unsupported macro status detected: {}", status);
            }
        }
        return result;
    }

    // the value is only available for servoport and backlightport
    public static byte getPortValue(LcOutputType outputType, byte value) {
        byte result = 0;

        switch (outputType) {
            case BACKLIGHTPORT:
            case SERVOPORT:
                result = value;
                break;
            default:
                break;
        }
        return result;
    }

}
