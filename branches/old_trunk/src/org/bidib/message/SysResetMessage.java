package org.bidib.message;

import org.bidib.BidibLibrary;

public class SysResetMessage extends BidibMessage {
    public SysResetMessage() {
        super(0, BidibLibrary.MSG_SYS_RESET, null);
    }
}
