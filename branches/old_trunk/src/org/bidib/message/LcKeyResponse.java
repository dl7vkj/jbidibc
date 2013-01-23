package org.bidib.message;

import org.bidib.exception.ProtocolException;

public class LcKeyResponse extends BidibMessage {
    LcKeyResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 2) {
            throw new ProtocolException("no lc key received");
        }
    }

    public int getKeyNumber() {
        return getData()[0];
    }

    public int getKeyState() {
        return getData()[1];
    }
}
