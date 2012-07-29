package org.bidib.message;

import org.bidib.BidibLibrary;

public class SysUniqueIdMessage extends BidibMessage {
    public SysUniqueIdMessage() {
        super(0, BidibLibrary.MSG_SYS_GET_UNIQUE_ID, null);
    }
}
