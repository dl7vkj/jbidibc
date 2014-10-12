package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.enumeration.BoosterState;
import org.bidib.jbidibc.core.exception.ProtocolException;

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

    public BoostStatResponse(byte[] addr, int num, BoosterState state) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_BOOST_STAT, state.getType());
    }

    public BoostStatResponse(byte[] addr, int num, byte state) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_BOOST_STAT, state);
    }

    public String getName() {
        return "MSG_BOOST_STAT";
    }

    public BoosterState getState() {
        return BoosterState.valueOf(getData()[0]);
    }
}
