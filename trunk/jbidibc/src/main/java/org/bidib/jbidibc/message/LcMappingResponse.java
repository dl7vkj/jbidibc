package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.LcMappingPortType;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

public class LcMappingResponse extends BidibMessage {

    public static final Integer TYPE = BidibLibrary.MSG_LC_MAPPING;

    LcMappingResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length < 2) {
            throw new ProtocolException("no MSG_LC_MAPPING received");
        }
    }

    public LcMappingResponse(byte[] addr, int num, byte portType, byte portCount, byte[] portAvailable,
        byte[] portMapping) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_LC_MAPPING, ByteUtils.concat(new byte[] { portType, portCount },
            ByteUtils.concat(portAvailable, portMapping)));
    }

    public String getName() {
        return "MSG_LC_MAPPING";
    }

    public LcMappingPortType getMappingPortType() {
        return LcMappingPortType.valueOf(getData()[0]);
    }

    public int getPortCount() {
        return ByteUtils.getInt(getData()[1]);
    }

    private int getFieldLen() {
        int portCount = getPortCount();
        if (portCount > 0) {
            return (portCount / 8) + 1;
        }
        return 0;
    }

    public byte[] getPortAvailable() {
        // TODO implememtation is missing
        return null;
    }

    public byte[] getPortMapping() {
        // TODO implememtation is missing
        return null;
    }
}
