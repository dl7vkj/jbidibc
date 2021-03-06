package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
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

    public int getMacroNumber() {
        return ByteUtils.getInt(getData()[0]);
    }

    /**
     * Get the parameter value at the specified index
     * @param index the index
     * @return the parameter value
     */
    public int getParameter(int index) {
        return ByteUtils.getInt(getData()[index]);
    }

    public byte[] getValue() {
        byte[] data = getData();
        byte[] result = new byte[data.length - 2];

        System.arraycopy(data, 2, result, 0, result.length);
        return result;
    }
}
