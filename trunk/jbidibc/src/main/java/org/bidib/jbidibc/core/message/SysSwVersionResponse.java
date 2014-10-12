package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.SoftwareVersion;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;

public class SysSwVersionResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_SYS_SW_VERSION;

    SysSwVersionResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 3) {
            throw new ProtocolException("no software version received");
        }
    }

    public SysSwVersionResponse(byte[] addr, int num, byte majorVersion, byte minorVersion, byte microVersion)
        throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_SYS_SW_VERSION, majorVersion, minorVersion, microVersion);
    }

    public String getName() {
        return "MSG_SYS_SW_VERSION";
    }

    public SoftwareVersion getVersion() {
        byte[] data = getData();

        return new SoftwareVersion(ByteUtils.getInt(data[2]), ByteUtils.getInt(data[1]), ByteUtils.getInt(data[0]));
    }
}
