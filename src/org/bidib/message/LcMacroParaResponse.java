package org.bidib.message;

import org.bidib.exception.ProtocolException;

public class LcMacroParaResponse extends BidibMessage {
    LcMacroParaResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 3) {
            throw new ProtocolException("no macro parameter received");
        }
    }

    public int getMacroNumber() {
        return getData()[0];
    }

    public int getParameter() {
        return getData()[1];
    }

    public int getValue() {
        return getData()[2];
    }
}
