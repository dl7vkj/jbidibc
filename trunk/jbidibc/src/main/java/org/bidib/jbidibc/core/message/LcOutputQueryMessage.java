package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.enumeration.LcOutputType;
import org.bidib.jbidibc.core.exception.ProtocolException;

public class LcOutputQueryMessage extends BidibCommandMessage {
    public LcOutputQueryMessage(LcOutputType outputType, int outputNumber) {
        super(0, BidibLibrary.MSG_LC_OUTPUT_QUERY, new byte[] { outputType.getType(), (byte) outputNumber });
    }

    public LcOutputQueryMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_LC_OUTPUT_QUERY";
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

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
