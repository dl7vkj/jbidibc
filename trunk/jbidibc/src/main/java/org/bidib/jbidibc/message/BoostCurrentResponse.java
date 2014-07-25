package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.MessageUtils;

/**
 * Response from booster with the current value
 */
@Deprecated
public class BoostCurrentResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_BOOST_CURRENT;

    BoostCurrentResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 1) {
            throw new ProtocolException("no booster current");
        }
    }

    public String getName() {
        return "MSG_BOOST_CURRENT";
    }

    public int getCurrent() {
        return MessageUtils.convertCurrent(getData()[0] & 0xFF);
    }
}
