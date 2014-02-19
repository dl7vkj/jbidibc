package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class SysResetMessage extends BidibMessage implements BidibCommand {
    public SysResetMessage() {
        super(0, BidibLibrary.MSG_SYS_RESET);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
