package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;

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
