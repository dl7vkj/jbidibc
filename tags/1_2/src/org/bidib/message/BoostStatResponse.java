package org.bidib.message;

import org.bidib.enumeration.BoosterState;
import org.bidib.exception.ProtocolException;

public class BoostStatResponse extends BidibMessage {
    BoostStatResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 1) {
            throw new ProtocolException("no booster state received");
        }
    }

    public BoosterState getState() {
        return BoosterState.valueOf(getData()[0]);
    }
}
