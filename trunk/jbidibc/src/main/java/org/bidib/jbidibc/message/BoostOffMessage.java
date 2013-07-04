package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

/**
 * Command to switch track voltage (signal) off
 */
public class BoostOffMessage extends BidibMessage {
    public static final byte BROADCAST_MESSAGE = 0;

    public static final byte BROADCAST_OFF = 1;

    @Deprecated
    public BoostOffMessage() {
        super(0, BidibLibrary.MSG_BOOST_OFF);
    }

    public BoostOffMessage(byte broadcast) {
        super(0, BidibLibrary.MSG_BOOST_OFF, new byte[] { (byte) broadcast });
    }
}
