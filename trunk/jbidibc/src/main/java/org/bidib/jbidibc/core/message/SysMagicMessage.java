package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;

public class SysMagicMessage extends BidibCommandMessage {
    protected SysMagicMessage() {
        super(0, BidibLibrary.MSG_SYS_GET_MAGIC);
    }

    public SysMagicMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_SYS_GET_MAGIC";
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { SysMagicResponse.TYPE };
    }
}
