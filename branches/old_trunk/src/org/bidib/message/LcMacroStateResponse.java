package org.bidib.message;

import org.bidib.enumeration.LcMacroState;
import org.bidib.exception.ProtocolException;

public class LcMacroStateResponse extends BidibMessage {
    LcMacroStateResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 2) {
            throw new ProtocolException("no macro state received");
        }
    }

    public int getMacroNumber() {
        return getData()[0];
    }

    public LcMacroState getMacroState() {
        return LcMacroState.valueOf(getData()[1]);
    }
}
