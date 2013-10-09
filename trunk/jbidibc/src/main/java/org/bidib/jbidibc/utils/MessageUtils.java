package org.bidib.jbidibc.utils;

import org.bidib.jbidibc.enumeration.AnalogPortEnum;
import org.bidib.jbidibc.enumeration.BacklightPortEnum;
import org.bidib.jbidibc.enumeration.BidibEnum;
import org.bidib.jbidibc.enumeration.LcOutputType;
import org.bidib.jbidibc.enumeration.LightPortEnum;
import org.bidib.jbidibc.enumeration.MotorPortEnum;
import org.bidib.jbidibc.enumeration.ServoPortEnum;
import org.bidib.jbidibc.enumeration.SoundPortEnum;
import org.bidib.jbidibc.enumeration.SwitchPortEnum;

public class MessageUtils {

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
        return result;
    }

}
