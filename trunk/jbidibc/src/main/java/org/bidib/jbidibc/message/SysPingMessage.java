package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;

public class SysPingMessage extends BidibCommandMessage {
    public SysPingMessage(byte marker) {
        super(0, BidibLibrary.MSG_SYS_PING, new byte[] { marker });
    }

    public SysPingMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_SYS_PING";
    }

    public byte getMarker() {
        return getData()[0];
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { SysPongResponse.TYPE };
    }
}
