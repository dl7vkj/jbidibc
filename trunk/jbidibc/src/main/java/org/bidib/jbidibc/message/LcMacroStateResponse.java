package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.LcMacroState;
import org.bidib.jbidibc.exception.ProtocolException;

public class LcMacroStateResponse extends BidibMessage {

    public static final Integer TYPE = BidibLibrary.MSG_LC_MACRO_STATE;

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
