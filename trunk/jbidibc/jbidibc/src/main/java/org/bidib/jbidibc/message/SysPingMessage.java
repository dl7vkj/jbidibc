package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class SysPingMessage extends BidibMessage {
    public SysPingMessage() {
        super(0, BidibLibrary.MSG_SYS_PING, new byte[] { 1 });
    }
}
