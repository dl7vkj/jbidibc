package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;

public class SysEnableMessage extends BidibCommandMessage {
    public SysEnableMessage() {
        super(0, BidibLibrary.MSG_SYS_ENABLE);
    }

    public SysEnableMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
