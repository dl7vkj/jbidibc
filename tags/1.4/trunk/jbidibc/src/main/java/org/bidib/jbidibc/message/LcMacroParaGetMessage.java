package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class LcMacroParaGetMessage extends BidibMessage {
    public LcMacroParaGetMessage(int macroNumber, int parameter) {
        super(0, BidibLibrary.MSG_LC_MACRO_PARA_GET, new byte[] { (byte) macroNumber, (byte) parameter });
    }
}
