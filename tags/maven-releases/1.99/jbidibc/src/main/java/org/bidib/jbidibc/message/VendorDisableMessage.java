package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class VendorDisableMessage extends BidibMessage {
    public VendorDisableMessage() {
        super(0, BidibLibrary.MSG_VENDOR_DISABLE, null);
    }
}
