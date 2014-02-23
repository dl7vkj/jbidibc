package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

public class LcMacroParaGetMessage extends BidibCommandMessage {
    protected LcMacroParaGetMessage(int macroNumber, int parameter) {
        super(0, BidibLibrary.MSG_LC_MACRO_PARA_GET, new byte[] { (byte) macroNumber, (byte) parameter });
    }

    public LcMacroParaGetMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public int getMacroNumber() {
        return ByteUtils.getInt(getData()[0]);
    }

    public int getParameterIndex() {
        return ByteUtils.getInt(getData()[1]);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { LcMacroParaResponse.TYPE };
    }
}
