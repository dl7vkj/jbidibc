package org.bidib.message;

import org.bidib.ProtocolVersion;
import org.bidib.exception.ProtocolException;

public class SysPVersionResponse extends BidibMessage {
    SysPVersionResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 2) {
            throw new ProtocolException("no protocol version received");
        }
    }

    public ProtocolVersion getVersion() {
        byte[] data = getData();

        return new ProtocolVersion((char) data[1], (char) data[0]);
    }
}
