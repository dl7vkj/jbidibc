package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.SoftwareVersion;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

public class SysSwVersionResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_SYS_SW_VERSION;

    SysSwVersionResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 3) {
            throw new ProtocolException("no software version received");
        }
    }

    public SoftwareVersion getVersion() {
        byte[] data = getData();

        return new SoftwareVersion(ByteUtils.getInt(data[2]), ByteUtils.getInt(data[1]), ByteUtils.getInt(data[0]));
    }
}
