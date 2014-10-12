package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;

public class LcKeyMessage extends BidibCommandMessage {
    public LcKeyMessage(int keyNumber) {
        super(0, BidibLibrary.MSG_LC_KEY_QUERY, new byte[] { (byte) keyNumber });
    }

    public LcKeyMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_LC_KEY_QUERY";
    }

    public int getKeyNumber() {
        return ByteUtils.getInt(getData()[0]);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
