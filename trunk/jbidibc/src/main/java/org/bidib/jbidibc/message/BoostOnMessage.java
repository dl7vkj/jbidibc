package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;

/**
 * Command to switch track voltage (signal) on
 */
public class BoostOnMessage extends BidibCommandMessage {
    public static final Integer TYPE = BidibLibrary.MSG_BOOST_ON;

    public static final byte BROADCAST_MESSAGE = 0;

    public static final byte BROADCAST_OFF = 1;

    public BoostOnMessage(byte broadcast) {
        super(0, BidibLibrary.MSG_BOOST_ON, new byte[] { (byte) broadcast });
    }

    public BoostOnMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_BOOST_ON";
    }

    public byte getBroadcast() {
        return getData()[0];
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
