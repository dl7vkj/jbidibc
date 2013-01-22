package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.LcMacroOperationCode;

public class LcMacroHandleMessage extends BidibMessage {
    public LcMacroHandleMessage(int macroNumber, LcMacroOperationCode macroOperationCode) {
        super(0, BidibLibrary.MSG_LC_MACRO_HANDLE, new byte[] { (byte) macroNumber, macroOperationCode.getType() });
    }
}
