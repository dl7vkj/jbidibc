package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.LcConfig;
import org.bidib.jbidibc.enumeration.LcOutputType;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

public class LcConfigResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_LC_CONFIG;

    LcConfigResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 6) {
            throw new ProtocolException("No LcConfig response received.");
        }
    }

    public LcConfigResponse(byte[] addr, int num, byte portType, byte portNum, byte[] config) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_LC_CONFIG, ByteUtils.concat(new byte[] { portType, portNum }, config));
    }

    public LcConfig getLcConfig() {
        byte[] data = getData();

        return new LcConfig(LcOutputType.valueOf(data[0]), ByteUtils.getInt(data[1]), ByteUtils.getInt(data[2]),
            ByteUtils.getInt(data[3]), ByteUtils.getInt(data[4]), ByteUtils.getInt(data[5]));
    }
}
