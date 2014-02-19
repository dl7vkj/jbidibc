package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.LcMacro;
import org.bidib.jbidibc.enumeration.LcOutputType;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
import org.bidib.jbidibc.utils.MessageUtils;

public class LcMacroSetMessage extends BidibMessage implements BidibCommand {

    /**
     * Creates a new LcMacroSetMessage instance with values from provided macro step.
     * @param macro the macro step.
     */
    public LcMacroSetMessage(LcMacro macro) {
        super(0, BidibLibrary.MSG_LC_MACRO_SET, new byte[] { macro.getMacroNumber(), macro.getStepNumber(),
            macro.getDelay(), macro.getOutputType().getType(), macro.getOutputNumber(),
            MessageUtils.getPortStatus(macro) });
    }

    public LcMacroSetMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public int getMacroNumber() {
        return ByteUtils.getInt(getData()[0]);
    }

    public int getStep() {
        return ByteUtils.getInt(getData()[1]);
    }

    /**
     * @return returns the initialized macro point
     */
    public LcMacro getMacro() {
        byte[] data = getData();
        LcOutputType outputType = LcOutputType.valueOf(data[3]);

        return new LcMacro(data[0], data[1], data[2], outputType, data[4], MessageUtils.toPortStatus(outputType,
            data[5]), MessageUtils.getPortValue(outputType, data[5]));
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { LcMacroResponse.TYPE };
    }
}
