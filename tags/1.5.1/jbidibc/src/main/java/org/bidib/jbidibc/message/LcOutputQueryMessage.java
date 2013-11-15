package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.LcOutputType;

public class LcOutputQueryMessage extends BidibMessage {
    public LcOutputQueryMessage(LcOutputType outputType, int outputNumber) {
        super(0, BidibLibrary.MSG_LC_OUTPUT_QUERY, new byte[] { outputType.getType(), (byte) outputNumber });
    }
}
