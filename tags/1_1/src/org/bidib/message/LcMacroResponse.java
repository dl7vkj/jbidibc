package org.bidib.message;

import org.bidib.LcMacro;
import org.bidib.enumeration.AnalogPortEnum;
import org.bidib.enumeration.BidibEnum;
import org.bidib.enumeration.LcOutputType;
import org.bidib.enumeration.LightPortEnum;
import org.bidib.enumeration.MotorPortEnum;
import org.bidib.enumeration.ServoPortEnum;
import org.bidib.enumeration.SoundPortEnum;
import org.bidib.enumeration.SwitchPortEnum;
import org.bidib.exception.ProtocolException;

public class LcMacroResponse extends BidibMessage {
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
        } else if (outputType == LcOutputType.LIGHTPORT) {
            result = LightPortEnum.valueOf(value);
        } else if (outputType == LcOutputType.MOTORPORT) {
            result = MotorPortEnum.valueOf(value);
        } else if (outputType == LcOutputType.SERVOPORT) {
            result = ServoPortEnum.START;
        } else if (outputType == LcOutputType.SOUNDPORT) {
            result = SoundPortEnum.valueOf(value);
        } else if (outputType == LcOutputType.SWITCHPORT) {
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
