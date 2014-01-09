package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.LcMacroOperationCode;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

public class LcMacroHandleMessage extends BidibMessage {
    public LcMacroHandleMessage(int macroNumber, LcMacroOperationCode macroOperationCode) {
        super(0, BidibLibrary.MSG_LC_MACRO_HANDLE, new byte[] { (byte) macroNumber, macroOperationCode.getType() });
    }

    public LcMacroHandleMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public int getMacroNumber() {
        return ByteUtils.getInt(getData()[0]);
    }

    public LcMacroOperationCode getMacroOperationCode() {
        LcMacroOperationCode lcMacroOperationCode = LcMacroOperationCode.valueOf(getData()[1]);
        return lcMacroOperationCode;
    }
}
