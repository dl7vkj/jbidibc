package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.LcMacro;
import org.bidib.jbidibc.enumeration.AnalogPortEnum;
import org.bidib.jbidibc.enumeration.BidibEnum;
import org.bidib.jbidibc.enumeration.LcOutputType;
import org.bidib.jbidibc.enumeration.LightPortEnum;
import org.bidib.jbidibc.enumeration.MotorPortEnum;
import org.bidib.jbidibc.enumeration.ServoPortEnum;
import org.bidib.jbidibc.enumeration.SoundPortEnum;
import org.bidib.jbidibc.enumeration.SwitchPortEnum;
import org.bidib.jbidibc.exception.ProtocolException;

public class LcMacroResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_LC_MACRO;

    LcMacroResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 6) {
            throw new ProtocolException("no macro received");
        }
    }

    public LcMacro getMacro() {
        byte[] data = getData();
        LcOutputType outputType = LcOutputType.valueOf(data[3]);

        return new LcMacro(data[0], data[1], data[2], outputType, data[4], getStatus(outputType, data[5]), getValue(
            outputType, data[5]));
    }

    private BidibEnum getStatus(LcOutputType outputType, byte value) {
        BidibEnum result = null;

        if (outputType == LcOutputType.ANALOGPORT) {
            result = AnalogPortEnum.valueOf(value);
        }
        else if (outputType == LcOutputType.LIGHTPORT) {
            result = LightPortEnum.valueOf(value);
        }
        else if (outputType == LcOutputType.MOTORPORT) {
            result = MotorPortEnum.valueOf(value);
        }
        else if (outputType == LcOutputType.SERVOPORT) {
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

    private byte getValue(LcOutputType outputType, byte value) {
        byte result = 0;

        if (outputType == LcOutputType.SERVOPORT) {
            result = value;
        }
        return result;
    }
}
