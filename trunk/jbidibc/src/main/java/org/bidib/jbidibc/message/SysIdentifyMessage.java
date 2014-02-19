package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.IdentifyState;

public class SysIdentifyMessage extends BidibMessage implements BidibCommand {
    public SysIdentifyMessage(IdentifyState state) {
        super(0, BidibLibrary.MSG_SYS_IDENTIFY, new byte[] { state.getType() });
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
