package org.bidib.message;

import org.bidib.BidibLibrary;

public class SysDisableMessage extends BidibMessage {
    public SysDisableMessage() {
        super(0, BidibLibrary.MSG_SYS_DISABLE, null);
    }
}
