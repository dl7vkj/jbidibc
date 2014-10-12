package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;

public class SysUniqueIdMessage extends BidibCommandMessage {
    public SysUniqueIdMessage() {
        super(0, BidibLibrary.MSG_SYS_GET_UNIQUE_ID);
    }

    public SysUniqueIdMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_SYS_GET_UNIQUE_ID";
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { SysUniqueIdResponse.TYPE };
    }
}
