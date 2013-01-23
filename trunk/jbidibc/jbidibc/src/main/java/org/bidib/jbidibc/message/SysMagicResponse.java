package org.bidib.jbidibc.message;

import org.bidib.jbidibc.exception.ProtocolException;

public class SysMagicResponse extends BidibMessage {
    SysMagicResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 2 || data[0] != (byte) 0xFE || data[1] != (byte) 0xAF) {
            throw new ProtocolException("no magic received");
        }
    }

    public int getMagic() {
        byte[] data = getData();

        return data[1] << 8 + data[0];
    }
}
