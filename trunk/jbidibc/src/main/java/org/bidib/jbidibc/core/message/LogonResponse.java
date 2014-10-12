package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;

public class LogonResponse extends SysUniqueIdResponse {
    public static final Integer TYPE = BidibLibrary.MSG_LOGON;

    LogonResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
    }

    public String getName() {
        return "MSG_LOGON";
    }
}
