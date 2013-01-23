package org.bidib.message;

import org.bidib.BidibLibrary;

public class SysGetPVersionMessage extends BidibMessage {
    public SysGetPVersionMessage() {
        super(0, BidibLibrary.MSG_SYS_GET_P_VERSION, null);
    }
}
