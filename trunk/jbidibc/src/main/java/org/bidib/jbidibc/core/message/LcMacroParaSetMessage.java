package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;

public class LcMacroParaSetMessage extends BidibCommandMessage {
    public LcMacroParaSetMessage(int macroNumber, int parameter, byte... value) {
        super(0, BidibLibrary.MSG_LC_MACRO_PARA_SET, ByteUtils.concat(
            new byte[] { (byte) macroNumber, (byte) parameter }, value));
    }

    public LcMacroParaSetMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_LC_MACRO_PARA_SET";
    }

    public int getMacroNumber() {
        return ByteUtils.getInt(getData()[0]);
    }

    public int getParameterIndex() {
        return ByteUtils.getInt(getData()[1]);
    }

    public byte[] getValue() {
        byte[] data = getData();
        byte[] result = new byte[data.length - 2];

        System.arraycopy(data, 2, result, 0, result.length);
        return result;
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { LcMacroParaResponse.TYPE };
    }
}
