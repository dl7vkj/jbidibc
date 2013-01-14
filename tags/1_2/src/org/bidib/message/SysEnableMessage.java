package org.bidib.message;

import org.bidib.BidibLibrary;

public class SysEnableMessage extends BidibMessage {
    public SysEnableMessage() {
        super(0, BidibLibrary.MSG_SYS_ENABLE, null);
    }
}
