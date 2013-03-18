package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class SysEnableMessage extends BidibMessage {
    public SysEnableMessage() {
        super(0, BidibLibrary.MSG_SYS_ENABLE);
    }
}
