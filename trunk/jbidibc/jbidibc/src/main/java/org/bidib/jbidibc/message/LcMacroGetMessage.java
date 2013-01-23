package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class LcMacroGetMessage extends BidibMessage {
    public LcMacroGetMessage(int macroNumber, int stepNumber) {
        super(0, BidibLibrary.MSG_LC_MACRO_GET, new byte[] { (byte) macroNumber, (byte) stepNumber });
    }
}
