package org.bidib.message;

import org.bidib.BidibLibrary;

public class LcMacroParaSetMessage extends BidibMessage {
    public LcMacroParaSetMessage(int macroNumber, int parameter, int value) {
        super(0, BidibLibrary.MSG_LC_MACRO_PARA_SET, new byte[] { (byte) macroNumber, (byte) parameter, (byte) value });
    }
}
