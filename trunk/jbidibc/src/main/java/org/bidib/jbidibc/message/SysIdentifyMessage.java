package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.IdentifyState;
import org.bidib.jbidibc.exception.ProtocolException;

public class SysIdentifyMessage extends BidibCommandMessage {
    public SysIdentifyMessage(IdentifyState state) {
        super(0, BidibLibrary.MSG_SYS_IDENTIFY, new byte[] { state.getType() });
    }

    public SysIdentifyMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_SYS_IDENTIFY";
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
