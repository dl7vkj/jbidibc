package org.bidib.jbidibc.message;

import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

public class LcKeyResponse extends BidibMessage {
    LcKeyResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 2) {
            throw new ProtocolException("no lc key received");
        }
    }

    public int getKeyNumber() {
        return ByteUtils.getInt(getData()[0]);
    }

    public int getKeyState() {
        return ByteUtils.getInt(getData()[1]);
    }
}
