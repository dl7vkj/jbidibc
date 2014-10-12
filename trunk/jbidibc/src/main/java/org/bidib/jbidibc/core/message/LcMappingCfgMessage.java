package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.enumeration.LcMappingPortType;
import org.bidib.jbidibc.core.exception.ProtocolException;

public class LcMappingCfgMessage extends BidibCommandMessage {

    public static final byte OPCODE_QUERY = (byte) 0;

    public static final byte OPCODE_CONFIG = (byte) 1;

    public LcMappingCfgMessage(LcMappingPortType mappingPortType) {
        super(0, BidibLibrary.MSG_LC_MAPPING_CFG, new byte[] { mappingPortType.getType(), OPCODE_QUERY });
    }

    public LcMappingCfgMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public LcMappingPortType getPortType() {
        return LcMappingPortType.valueOf(getData()[0]);
    }

    public String getName() {
        return "MSG_LC_MAPPING_CFG";
    }

    public byte getOpCode() {
        return getData()[1];
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { LcMappingResponse.TYPE };
    }
}
