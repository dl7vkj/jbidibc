package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;

public class SysDisableMessage extends BidibCommandMessage {
    protected SysDisableMessage() {
        super(0, BidibLibrary.MSG_SYS_DISABLE);
    }

    public SysDisableMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
