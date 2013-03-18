package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class SysResetMessage extends BidibMessage {
    public SysResetMessage() {
        super(0, BidibLibrary.MSG_SYS_RESET);
    }
}
