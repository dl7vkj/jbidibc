package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;

public class SysGetSwVersionMessage extends BidibCommandMessage {
    public SysGetSwVersionMessage() {
        super(0, BidibLibrary.MSG_SYS_GET_SW_VERSION);
    }

    public SysGetSwVersionMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_SYS_GET_SW_VERSION";
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { SysSwVersionResponse.TYPE };
    }
}
