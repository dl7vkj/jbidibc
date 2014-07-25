package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.LcOutputType;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

public class LcStatResponse extends BidibMessage {

    public static final Integer TYPE = BidibLibrary.MSG_LC_STAT;

    LcStatResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 3) {
            throw new ProtocolException("no lc stat received");
        }
    }

    public LcStatResponse(byte[] addr, int num, byte portType, byte portNum, byte portStatus) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_LC_STAT, new byte[] { portType, portNum, portStatus });
    }

    public String getName() {
        return "MSG_LC_STAT";
    }

    public LcOutputType getPortType() {
        return LcOutputType.valueOf(getData()[0]);
    }

    public int getPortNumber() {
        return ByteUtils.getInt(getData()[1]);
    }

    public int getPortStatus() {
        return ByteUtils.getInt(getData()[2]);
    }
}
