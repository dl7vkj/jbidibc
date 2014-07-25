package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;

public class SysGetPVersionMessage extends BidibCommandMessage {
    public SysGetPVersionMessage() {
        super(0, BidibLibrary.MSG_SYS_GET_P_VERSION);
    }

    public SysGetPVersionMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_SYS_GET_P_VERSION";
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { SysPVersionResponse.TYPE };
    }
}
