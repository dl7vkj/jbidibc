package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.enumeration.LcOutputType;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;

public class LcNotAvailableResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_LC_NA;

    LcNotAvailableResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 2) {
            throw new ProtocolException("LC not available response received");
        }
    }

    public LcNotAvailableResponse(byte[] addr, int num, byte portType, byte portNum) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_LC_NA, portType, portNum);
    }

    public String getName() {
        return "MSG_LC_NA";
    }

    public LcOutputType getPortType() {
        return LcOutputType.valueOf(getData()[0]);
    }

    public int getPortNumber() {
        return ByteUtils.getInt(getData()[1]);
    }
}
