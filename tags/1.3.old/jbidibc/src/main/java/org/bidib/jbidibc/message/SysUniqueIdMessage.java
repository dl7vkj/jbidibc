package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class SysUniqueIdMessage extends BidibMessage {
    public SysUniqueIdMessage() {
        super(0, BidibLibrary.MSG_SYS_GET_UNIQUE_ID);
    }
}
