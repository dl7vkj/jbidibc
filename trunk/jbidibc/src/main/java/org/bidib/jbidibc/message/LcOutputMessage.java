package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.LcOutputType;
import org.bidib.jbidibc.exception.ProtocolException;

public class LcOutputMessage extends BidibMessage implements BidibCommand {
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

    @Override
    public Integer[] getExpectedResponseTypes() {
        // the response MSG_LC_STAT is signaled asynchronously 
        return null;
    }
}
