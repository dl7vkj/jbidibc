package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.enumeration.LcOutputType;
import org.bidib.jbidibc.core.exception.ProtocolException;

public class LcOutputMessage extends BidibCommandMessage {
    public LcOutputMessage(LcOutputType outputType, int outputNumber, int status) {
        super(0, BidibLibrary.MSG_LC_OUTPUT, new byte[] { outputType.getType(), (byte) outputNumber, (byte) status });
    }

    public LcOutputMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public LcOutputType getPortType() {
        return LcOutputType.valueOf(getData()[0]);
    }

    public String getName() {
        return "MSG_LC_OUTPUT";
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
