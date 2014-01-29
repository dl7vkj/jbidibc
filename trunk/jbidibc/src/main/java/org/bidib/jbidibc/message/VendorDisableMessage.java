package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;

public class VendorDisableMessage extends BidibMessage {
    public VendorDisableMessage() {
        super(0, BidibLibrary.MSG_VENDOR_DISABLE);
    }

    public VendorDisableMessage(byte[] message) throws ProtocolException {
        super(message);
    }
}
