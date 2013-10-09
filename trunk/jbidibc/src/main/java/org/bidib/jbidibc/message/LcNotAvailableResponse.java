package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;

public class LcNotAvailableResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_LC_NA;

    LcNotAvailableResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 2) {
            throw new ProtocolException("LC not available response received");
        }
    }

    public byte getPortType() {
        return getData()[0];
    }

    public byte getPortNumber() {
        return getData()[1];
    }
}
