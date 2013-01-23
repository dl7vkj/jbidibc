package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class SysGetPVersionMessage extends BidibMessage {
    public SysGetPVersionMessage() {
        super(0, BidibLibrary.MSG_SYS_GET_P_VERSION);
    }
}
