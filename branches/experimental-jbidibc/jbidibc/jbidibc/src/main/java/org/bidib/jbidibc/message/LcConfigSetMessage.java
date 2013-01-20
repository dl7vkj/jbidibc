package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.LcConfig;

public class LcConfigSetMessage extends BidibMessage {
    public LcConfigSetMessage(LcConfig config) {
        super(0, BidibLibrary.MSG_LC_CONFIG_SET, new byte[] { config.getOutputType().getType(),
                (byte) config.getOutputNumber(), (byte) config.getValue1(), (byte) config.getValue2(),
                (byte) config.getValue3(), (byte) config.getValue4() });
    }
}
