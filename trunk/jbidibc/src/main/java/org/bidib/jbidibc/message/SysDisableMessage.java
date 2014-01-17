package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;

public class SysDisableMessage extends BidibMessage {
    public SysDisableMessage() {
        super(0, BidibLibrary.MSG_SYS_DISABLE);
    }

    public SysDisableMessage(byte[] message) throws ProtocolException {
        super(message);
    }
}
