package org.bidib.jbidibc.message;

import org.bidib.jbidibc.exception.ProtocolException;

/**
 * Response from booster with the current value
 */
@Deprecated
public class BoostCurrentResponse extends BidibMessage {
    BoostCurrentResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 1) {
            throw new ProtocolException("no booster current");
        }
    }

    public static int convertCurrent(int current) {
        if (current > 15) {
            if (current >= 16 && current <= 63) {
                current = (current - 12) * 4;
            }
            else if (current >= 64 && current <= 127) {
                current = (current - 51) * 16;
            }
            else if (current >= 128 && current <= 191) {
                current = (current - 108) * 64;
            }
            else if (current >= 192 && current <= 250) {
                current = (current - 171) * 256;
            }
            else {
                current = 0;
            }
        }
        return current;
    }

    public int getCurrent() {
        return convertCurrent(getData()[0] & 0xFF);
    }
}
