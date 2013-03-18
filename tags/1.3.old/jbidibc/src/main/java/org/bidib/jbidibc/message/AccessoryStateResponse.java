package org.bidib.jbidibc.message;

import org.bidib.jbidibc.AccessoryState;
import org.bidib.jbidibc.exception.ProtocolException;

public class AccessoryStateResponse extends BidibMessage {
    AccessoryStateResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 5) {
            throw new ProtocolException("no accessory state received");
        }
    }

    public AccessoryState getAccessoryState() {
        byte[] data = getData();

        return new AccessoryState(data[0], data[1], data[2], data[3], data[4]);
    }
}
