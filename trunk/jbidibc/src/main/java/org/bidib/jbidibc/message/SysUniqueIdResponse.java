package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;

public class SysUniqueIdResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_SYS_UNIQUE_ID;

    SysUniqueIdResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 7) {
            throw new ProtocolException("no unique id received");
        }
    }

    public byte[] getUniqueId() {
        return getData();
    }
}
