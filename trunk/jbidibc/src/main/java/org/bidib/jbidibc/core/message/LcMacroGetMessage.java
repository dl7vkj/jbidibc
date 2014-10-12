package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;

public class LcMacroGetMessage extends BidibCommandMessage {
    public LcMacroGetMessage(int macroNumber, int stepNumber) {
        super(0, BidibLibrary.MSG_LC_MACRO_GET, new byte[] { (byte) macroNumber, (byte) stepNumber });
    }

    public LcMacroGetMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_LC_MACRO_GET";
    }

    public int getMacroNumber() {
        return ByteUtils.getInt(getData()[0]);
    }

    public int getStep() {
        return ByteUtils.getInt(getData()[1]);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { LcMacroResponse.TYPE };
    }
}
