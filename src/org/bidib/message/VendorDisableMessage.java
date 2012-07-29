package org.bidib.message;

import org.bidib.BidibLibrary;

public class VendorDisableMessage extends BidibMessage {
    public VendorDisableMessage() {
        super(0, BidibLibrary.MSG_VENDOR_DISABLE, null);
    }
}
