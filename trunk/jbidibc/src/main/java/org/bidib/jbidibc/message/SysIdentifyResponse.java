package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.IdentifyState;
import org.bidib.jbidibc.exception.ProtocolException;

public class SysIdentifyResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_SYS_IDENTIFY_STATE;

    SysIdentifyResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 1) {
            throw new ProtocolException("no identify state received");
        }
    }

    public String getName() {
        return "MSG_SYS_IDENTIFY_STATE";
    }

    public IdentifyState getState() {
        return IdentifyState.valueOf(getData()[0]);
    }
}
