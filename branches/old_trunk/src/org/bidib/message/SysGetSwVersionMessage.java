package org.bidib.message;

import org.bidib.BidibLibrary;

public class SysGetSwVersionMessage extends BidibMessage {
    public SysGetSwVersionMessage() {
        super(0, BidibLibrary.MSG_SYS_GET_SW_VERSION, null);
    }
}
