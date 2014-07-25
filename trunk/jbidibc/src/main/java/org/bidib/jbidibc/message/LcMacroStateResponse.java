package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.LcMacroState;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

public class LcMacroStateResponse extends BidibMessage {

    public static final Integer TYPE = BidibLibrary.MSG_LC_MACRO_STATE;

    LcMacroStateResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 2) {
            throw new ProtocolException("no macro state received");
        }
    }

    public LcMacroStateResponse(byte[] addr, int num, byte macroNumber, LcMacroState macroState)
        throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_LC_MACRO_STATE, new byte[] { macroNumber, macroState.getType() });
    }

    public String getName() {
        return "MSG_LC_MACRO_STATE";
    }

    public int getMacroNumber() {
        return ByteUtils.getInt(getData()[0]);
    }

    public LcMacroState getMacroState() {
        return LcMacroState.valueOf(getData()[1]);
    }

    public String toExtendedString() {
        StringBuffer sb = new StringBuffer("[ ");
        sb.append(super.toString());
        sb.append(", macroNumber: ").append(getMacroNumber());
        sb.append(", macroState: ").append(getMacroState());
        return sb.toString();
    }

}
