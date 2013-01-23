package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class SysMagicMessage extends BidibMessage {
    public SysMagicMessage() {
        super(0, BidibLibrary.MSG_SYS_GET_MAGIC);
    }
}
