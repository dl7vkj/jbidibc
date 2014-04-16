package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;

public class SysResetMessage extends BidibCommandMessage {
    public SysResetMessage() {
        super(0, BidibLibrary.MSG_SYS_RESET);
    }

    public SysResetMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
