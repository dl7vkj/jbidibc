package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.ProtocolVersion;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

public class SysPVersionResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_SYS_P_VERSION;

    SysPVersionResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 2) {
            throw new ProtocolException("no protocol version received");
        }
    }

    public ProtocolVersion getVersion() {
        byte[] data = getData();

        return new ProtocolVersion(ByteUtils.getInt(data[1]), ByteUtils.getInt(data[0]));
    }
}
