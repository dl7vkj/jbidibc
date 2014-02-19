package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class SysPingMessage extends BidibMessage implements BidibCommand {
    public SysPingMessage(byte marker) {
        super(0, BidibLibrary.MSG_SYS_PING, new byte[] { marker });
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { SysPongResponse.TYPE };
    }
}
