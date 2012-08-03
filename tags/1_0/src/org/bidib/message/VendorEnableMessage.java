package org.bidib.message;

import org.bidib.BidibLibrary;

public class VendorEnableMessage extends BidibMessage {
    public VendorEnableMessage(byte[] uniqueId) {
        super(0, BidibLibrary.MSG_VENDOR_ENABLE, uniqueId);
    }
}
