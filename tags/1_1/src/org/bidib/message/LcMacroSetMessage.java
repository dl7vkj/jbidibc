package org.bidib.message;

import org.bidib.BidibLibrary;
import org.bidib.LcMacro;
import org.bidib.enumeration.AnalogPortEnum;
import org.bidib.enumeration.BidibEnum;
import org.bidib.enumeration.FlagEnum;
import org.bidib.enumeration.LightPortEnum;
import org.bidib.enumeration.MotorPortEnum;
import org.bidib.enumeration.ServoPortEnum;
import org.bidib.enumeration.SoundPortEnum;
import org.bidib.enumeration.SwitchPortEnum;

public class LcMacroSetMessage extends BidibMessage {
    public LcMacroSetMessage(LcMacro macro) {
        super(0, BidibLibrary.MSG_LC_MACRO_SET, new byte[] { macro.getMacroNumber(), macro.getStepNumber(),
                macro.getDelay(), macro.getOutputType().getType(), macro.getOutputNumber(), getPortStatus(macro) });
    }

    private static byte getPortStatus(LcMacro macro) {
        byte result = 0;
        BidibEnum status = macro.getStatus();

        if (status != null) {
            if (status instanceof AnalogPortEnum) {
                result = ((AnalogPortEnum) status).getType();
            } else if (status instanceof FlagEnum) {
                result = ((FlagEnum) status).getType();
            } else if (status instanceof LightPortEnum) {
                result = ((LightPortEnum) status).getType();
            } else if (status instanceof MotorPortEnum) {
                result = ((MotorPortEnum) status).getType();
            } else if (status instanceof ServoPortEnum) {
                result = macro.getValue();
            } else if (status instanceof SoundPortEnum) {
                result = ((SoundPortEnum) status).getType();
            } else if (status instanceof SwitchPortEnum) {
                result = ((SwitchPortEnum) status).getType();
            }
        }
        return result;
    }
}
