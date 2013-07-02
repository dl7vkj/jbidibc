package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;

public class SysPongResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_SYS_PONG;

    SysPongResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 1) {
            throw new ProtocolException("invalid data in pong response");
        }
    }
}
