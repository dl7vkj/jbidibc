package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

public class LcMacroGetMessage extends BidibMessage {
    public LcMacroGetMessage(int macroNumber, int stepNumber) {
        super(0, BidibLibrary.MSG_LC_MACRO_GET, new byte[] { (byte) macroNumber, (byte) stepNumber });
    }

    public LcMacroGetMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public int getMacroNumber() {
        return ByteUtils.getInt(getData()[0]);
    }

    public int getStep() {
        return ByteUtils.getInt(getData()[1]);
    }
}
