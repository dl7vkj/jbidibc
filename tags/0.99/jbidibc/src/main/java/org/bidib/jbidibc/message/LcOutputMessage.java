package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.LcOutputType;

public class LcOutputMessage extends BidibMessage {
    public LcOutputMessage(LcOutputType outputType, int outputNumber, int status) {
        super(0, BidibLibrary.MSG_LC_OUTPUT, new byte[] { outputType.getType(), (byte) outputNumber, (byte) status });
    }
}
