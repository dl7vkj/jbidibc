package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;

public class LogonResponse extends SysUniqueIdResponse {
    public static final Integer TYPE = BidibLibrary.MSG_LOGON;

    LogonResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
    }
}
