package org.bidib.message;

import org.bidib.BidibLibrary;
import org.bidib.enumeration.LcMacroOperationCode;

public class LcMacroHandleMessage extends BidibMessage {
    public LcMacroHandleMessage(int macroNumber, LcMacroOperationCode macroOperationCode) {
        super(0, BidibLibrary.MSG_LC_MACRO_HANDLE, new byte[] { (byte) macroNumber, macroOperationCode.getType() });
    }
}
