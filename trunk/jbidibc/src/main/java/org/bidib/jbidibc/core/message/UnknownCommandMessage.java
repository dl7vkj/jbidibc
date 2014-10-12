package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.exception.ProtocolException;

public class UnknownCommandMessage extends BidibCommandMessage {

    UnknownCommandMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }

}
