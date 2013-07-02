package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.BoosterState;
import org.bidib.jbidibc.exception.ProtocolException;

/**
 * Response from booster with the status
 */
public class BoostStatResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_BOOST_STAT;

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
