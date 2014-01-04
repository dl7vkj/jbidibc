package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.LcOutputType;
import org.bidib.jbidibc.exception.ProtocolException;

public class LcOutputMessage extends BidibMessage {
    public LcOutputMessage(LcOutputType outputType, int outputNumber, int status) {
        super(0, BidibLibrary.MSG_LC_OUTPUT, new byte[] { outputType.getType(), (byte) outputNumber, (byte) status });
    }

    public LcOutputMessage(byte[] message) throws ProtocolException {
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

    public byte getOutputStatus() {
        return getData()[2];
    }
}
