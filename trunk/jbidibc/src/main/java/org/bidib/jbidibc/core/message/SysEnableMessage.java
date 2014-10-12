package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;

public class SysEnableMessage extends BidibCommandMessage {
    protected SysEnableMessage() {
        super(0, BidibLibrary.MSG_SYS_ENABLE);
    }

    public SysEnableMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_SYS_ENABLE";
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
