package org.bidib.jbidibc.message;

import org.bidib.jbidibc.exception.ProtocolException;

public class LogonResponse extends SysUniqueIdResponse {
    LogonResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
    }
}
