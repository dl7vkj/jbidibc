package org.bidib.message;

import org.bidib.exception.ProtocolException;

public class SysUniqueIdResponse extends BidibMessage {
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
