package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class SysGetSwVersionMessage extends BidibMessage {
    public SysGetSwVersionMessage() {
        super(0, BidibLibrary.MSG_SYS_GET_SW_VERSION, null);
    }
}
