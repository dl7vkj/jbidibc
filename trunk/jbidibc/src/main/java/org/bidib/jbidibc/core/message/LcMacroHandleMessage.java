package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.enumeration.LcMacroOperationCode;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;

public class LcMacroHandleMessage extends BidibCommandMessage {
    public LcMacroHandleMessage(int macroNumber, LcMacroOperationCode macroOperationCode) {
        super(0, BidibLibrary.MSG_LC_MACRO_HANDLE, new byte[] { (byte) macroNumber, macroOperationCode.getType() });
    }

    public LcMacroHandleMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_LC_MACRO_HANDLE";
    }

    public int getMacroNumber() {
        return ByteUtils.getInt(getData()[0]);
    }

    public LcMacroOperationCode getMacroOperationCode() {
        LcMacroOperationCode lcMacroOperationCode = LcMacroOperationCode.valueOf(getData()[1]);
        return lcMacroOperationCode;
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { LcMacroStateResponse.TYPE };
    }
}
