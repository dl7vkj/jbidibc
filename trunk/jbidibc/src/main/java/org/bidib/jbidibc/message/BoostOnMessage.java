package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

/**
 * Command to switch track voltage (signal) on
 */
public class BoostOnMessage extends BidibCommandMessage {
    public static final byte BROADCAST_MESSAGE = 0;

    public static final byte BROADCAST_OFF = 1;

    public BoostOnMessage(byte broadcast) {
        super(0, BidibLibrary.MSG_BOOST_ON, new byte[] { (byte) broadcast });
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
