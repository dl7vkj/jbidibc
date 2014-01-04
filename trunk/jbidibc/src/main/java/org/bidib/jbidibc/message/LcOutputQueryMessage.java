package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.LcOutputType;
import org.bidib.jbidibc.exception.ProtocolException;

public class LcOutputQueryMessage extends BidibMessage {
    public LcOutputQueryMessage(LcOutputType outputType, int outputNumber) {
        super(0, BidibLibrary.MSG_LC_OUTPUT_QUERY, new byte[] { outputType.getType(), (byte) outputNumber });
    }

    public LcOutputQueryMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public LcOutputType getPortType() {
        return LcOutputType.valueOf(getData()[0]);
    }

    public byte getOutputType() {
        return getData()[0];
    }

    public byte getOutputNum() {
        return getData()[1];
    }
}
