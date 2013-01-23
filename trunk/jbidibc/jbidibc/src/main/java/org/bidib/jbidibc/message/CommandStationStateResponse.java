package org.bidib.jbidibc.message;

import org.bidib.jbidibc.exception.ProtocolException;

public class CommandStationStateResponse extends BidibMessage {
    CommandStationStateResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 1) {
            throw new ProtocolException("no command station state received");
        }
    }

    public int getState() {
        return getData()[0] & 0xFF;
    }
}
