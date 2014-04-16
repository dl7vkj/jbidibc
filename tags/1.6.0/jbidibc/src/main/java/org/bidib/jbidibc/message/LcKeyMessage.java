package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

public class LcKeyMessage extends BidibCommandMessage {
    public LcKeyMessage(int keyNumber) {
        super(0, BidibLibrary.MSG_LC_KEY_QUERY, new byte[] { (byte) keyNumber });
    }

    public LcKeyMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public int getKeyNumber() {
        return ByteUtils.getInt(getData()[0]);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
