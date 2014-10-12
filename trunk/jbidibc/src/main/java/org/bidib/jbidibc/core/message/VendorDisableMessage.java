package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;

public class VendorDisableMessage extends BidibCommandMessage {
    public VendorDisableMessage() {
        super(0, BidibLibrary.MSG_VENDOR_DISABLE);
    }

    public VendorDisableMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_VENDOR_DISABLE";
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { VendorAckResponse.TYPE };
    }
}
