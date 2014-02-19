package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.LcOutputType;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

public class LcConfigGetMessage extends BidibCommandMessage {
    public LcConfigGetMessage(LcOutputType outputType, int outputNumber) {
        super(0, BidibLibrary.MSG_LC_CONFIG_GET, new byte[] { outputType.getType(), (byte) outputNumber });
    }

    public LcConfigGetMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public LcOutputType getPortType() {
        return LcOutputType.valueOf(getData()[0]);
    }

    public int getPortNumber() {
        return ByteUtils.getInt(getData()[1]);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { LcConfigResponse.TYPE, LcNotAvailableResponse.TYPE };
    }
}
