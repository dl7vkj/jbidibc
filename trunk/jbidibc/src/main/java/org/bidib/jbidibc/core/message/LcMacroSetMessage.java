package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.LcMacro;
import org.bidib.jbidibc.core.enumeration.LcOutputType;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.bidib.jbidibc.core.utils.MessageUtils;

public class LcMacroSetMessage extends BidibCommandMessage {

    /**
     * Creates a new LcMacroSetMessage instance with values from provided macro step.
     * 
     * @param macro
     *            the macro step.
     */
    public LcMacroSetMessage(LcMacro macro) {
        super(0, BidibLibrary.MSG_LC_MACRO_SET, new byte[] { macro.getMacroNumber(), macro.getStepNumber(),
            macro.getDelay(), macro.getOutputType().getType(), macro.getOutputNumber(),
            MessageUtils.getPortStatus(macro) });
    }

    public LcMacroSetMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_LC_MACRO_SET";
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
