package org.bidib.jbidibc.message;

import org.bidib.jbidibc.exception.ProtocolException;

public class UnknownCommandMessage extends BidibCommandMessage {

    UnknownCommandMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }

}
