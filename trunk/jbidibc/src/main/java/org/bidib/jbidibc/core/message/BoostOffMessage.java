package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;

/**
 * Command to switch track voltage (signal) off
 */
public class BoostOffMessage extends BidibCommandMessage {
    public static final Integer TYPE = BidibLibrary.MSG_BOOST_OFF;

    public static final byte BROADCAST_MESSAGE = 0;

    public static final byte BROADCAST_OFF = 1;

    public BoostOffMessage(byte broadcast) {
        super(0, BidibLibrary.MSG_BOOST_OFF, new byte[] { (byte) broadcast });
    }

    public BoostOffMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_BOOST_OFF";
    }

    public byte getBroadcast() {
        return getData()[0];
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
