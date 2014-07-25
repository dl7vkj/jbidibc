package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.LcMacroParaValue;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

public class LcMacroParaResponse extends BidibMessage {

    public static final Integer TYPE = BidibLibrary.MSG_LC_MACRO_PARA;

    LcMacroParaResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length < 3) {
            throw new ProtocolException("no macro parameter received");
        }
    }

    public LcMacroParaResponse(byte[] addr, int num, byte macroNumber, byte step, byte[] value)
        throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_LC_MACRO_PARA, ByteUtils.concat(new byte[] { macroNumber, step }, value));
    }

    public String getName() {
        return "MSG_LC_MACRO_PARA";
    }

    public int getMacroNumber() {
        return ByteUtils.getInt(getData()[0]);
    }

    /**
     * Get the parameter index
     * 
     * @return the parameter index
     */
    public int getParameterIndex() {
        return ByteUtils.getInt(getData()[1]);
    }

    /**
     * Get the parameter value
     * 
     * @return the parameter value
     */
    public LcMacroParaValue getLcMacroParaValue() {
        byte[] data = getData();
        byte[] result = new byte[data.length - 2];

        System.arraycopy(data, 2, result, 0, result.length);
        return new LcMacroParaValue(getMacroNumber(), getParameterIndex(), result);
    }
}
