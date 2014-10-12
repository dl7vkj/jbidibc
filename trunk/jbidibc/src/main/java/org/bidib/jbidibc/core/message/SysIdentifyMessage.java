package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.enumeration.IdentifyState;
import org.bidib.jbidibc.core.exception.ProtocolException;

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
