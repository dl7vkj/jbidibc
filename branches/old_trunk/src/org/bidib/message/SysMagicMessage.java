package org.bidib.message;

import org.bidib.BidibLibrary;

public class SysMagicMessage extends BidibMessage {
    public SysMagicMessage() {
        super(0, BidibLibrary.MSG_SYS_GET_MAGIC, null);
    }
}
