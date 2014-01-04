package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;

public class SysMagicMessage extends BidibMessage {
    public SysMagicMessage() {
        super(0, BidibLibrary.MSG_SYS_GET_MAGIC);
    }

    public SysMagicMessage(byte[] message) throws ProtocolException {
        super(message);
    }
}
