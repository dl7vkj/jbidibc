package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.LcConfig;
import org.bidib.jbidibc.core.enumeration.LcOutputType;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;

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

    public String getName() {
        return "MSG_LC_CONFIG";
    }

    public LcConfig getLcConfig() {
        byte[] data = getData();

        // the MB is the flag for inactive ports
        byte outputType = (byte) (data[0] & 0x7F);
        boolean inActive = ((data[0] & 0x80) == 0x80);

        return new LcConfig(LcOutputType.valueOf(outputType), inActive, ByteUtils.getInt(data[1]),
            ByteUtils.getInt(data[2]), ByteUtils.getInt(data[3]), ByteUtils.getInt(data[4]), ByteUtils.getInt(data[5]));
    }
}
