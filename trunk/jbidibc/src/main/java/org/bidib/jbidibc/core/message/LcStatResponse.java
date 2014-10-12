package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.enumeration.LcOutputType;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;

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
