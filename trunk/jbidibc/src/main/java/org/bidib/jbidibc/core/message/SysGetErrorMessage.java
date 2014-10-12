package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;

public class SysGetErrorMessage extends BidibCommandMessage {
    protected SysGetErrorMessage() {
        super(0, BidibLibrary.MSG_SYS_GET_ERROR);
    }

    public SysGetErrorMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_SYS_GET_ERROR";
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
