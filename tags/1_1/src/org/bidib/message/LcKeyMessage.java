package org.bidib.message;

import org.bidib.BidibLibrary;

public class LcKeyMessage extends BidibMessage {
    public LcKeyMessage(int keyNumber) {
        super(0, BidibLibrary.MSG_LC_KEY_QUERY, new byte[] { (byte) keyNumber });
    }
}
