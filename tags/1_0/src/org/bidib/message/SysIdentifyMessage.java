package org.bidib.message;

import org.bidib.BidibLibrary;
import org.bidib.enumeration.IdentifyState;

public class SysIdentifyMessage extends BidibMessage {
    public SysIdentifyMessage(IdentifyState state) {
        super(0, BidibLibrary.MSG_SYS_IDENTIFY, new byte[] { state.getType() });
    }
}
