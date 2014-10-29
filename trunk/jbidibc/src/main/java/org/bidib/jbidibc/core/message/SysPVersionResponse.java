package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.ProtocolVersion;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;

public class SysPVersionResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_SYS_P_VERSION;

    SysPVersionResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 2) {
            throw new ProtocolException("no protocol version received");
        }
    }

    public SysPVersionResponse(byte[] addr, int num, byte majorVersion, byte minorVersion) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_SYS_P_VERSION, minorVersion, majorVersion);
    }

    public String getName() {
        return "MSG_SYS_P_VERSION";
    }

    public ProtocolVersion getVersion() {
        byte[] data = getData();

        return new ProtocolVersion(ByteUtils.getInt(data[1]), ByteUtils.getInt(data[0]));
    }
}
