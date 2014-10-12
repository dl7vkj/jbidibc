package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;

public class SysDisableMessage extends BidibCommandMessage {
    protected SysDisableMessage() {
        super(0, BidibLibrary.MSG_SYS_DISABLE);
    }

    public SysDisableMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_SYS_DISABLE";
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
