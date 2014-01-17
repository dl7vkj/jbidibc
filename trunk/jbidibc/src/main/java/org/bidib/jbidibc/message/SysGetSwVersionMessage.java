package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;

public class SysGetSwVersionMessage extends BidibMessage {
    public SysGetSwVersionMessage() {
        super(0, BidibLibrary.MSG_SYS_GET_SW_VERSION);
    }

    public SysGetSwVersionMessage(byte[] message) throws ProtocolException {
        super(message);
    }
}
