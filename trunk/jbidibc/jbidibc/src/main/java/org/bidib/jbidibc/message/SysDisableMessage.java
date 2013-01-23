package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class SysDisableMessage extends BidibMessage {
    public SysDisableMessage() {
        super(0, BidibLibrary.MSG_SYS_DISABLE);
    }
}
