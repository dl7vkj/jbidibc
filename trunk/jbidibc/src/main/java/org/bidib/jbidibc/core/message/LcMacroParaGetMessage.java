package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;

public class LcMacroParaGetMessage extends BidibCommandMessage {
    protected LcMacroParaGetMessage(int macroNumber, int parameter) {
        super(0, BidibLibrary.MSG_LC_MACRO_PARA_GET, new byte[] { (byte) macroNumber, (byte) parameter });
    }

    public LcMacroParaGetMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_LC_MACRO_PARA_GET";
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
