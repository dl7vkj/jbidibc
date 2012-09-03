package org.bidib.message;

import org.bidib.exception.ProtocolException;

public class LcMacroParaResponse extends BidibMessage {
    LcMacroParaResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length < 3) {
            throw new ProtocolException("no macro parameter received");
        }
    }

    public int getMacroNumber() {
        return getData()[0];
    }

    public int getParameter() {
        return getData()[1];
    }

    public byte[] getValue() {
        byte[] data = getData();
        byte[] result = new byte[data.length - 2];

        System.arraycopy(data, 2, result, 0, result.length);
        return result;
    }
}
