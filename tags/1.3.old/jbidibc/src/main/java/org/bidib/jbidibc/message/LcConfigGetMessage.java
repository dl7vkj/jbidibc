package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.LcOutputType;

public class LcConfigGetMessage extends BidibMessage {
    public LcConfigGetMessage(LcOutputType outputType, int outputNumber) {
        super(0, BidibLibrary.MSG_LC_CONFIG_GET, new byte[] { outputType.getType(), (byte) outputNumber });
    }
}
