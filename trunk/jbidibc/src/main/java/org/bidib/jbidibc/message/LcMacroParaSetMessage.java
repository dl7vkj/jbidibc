package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

public class LcMacroParaSetMessage extends BidibMessage {
    public LcMacroParaSetMessage(int macroNumber, int parameter, byte... value) {
        super(0, BidibLibrary.MSG_LC_MACRO_PARA_SET, ByteUtils.concat(
            new byte[] { (byte) macroNumber, (byte) parameter }, value));
    }

    public LcMacroParaSetMessage(byte[] message) throws ProtocolException {
        super(message);
    }

}
