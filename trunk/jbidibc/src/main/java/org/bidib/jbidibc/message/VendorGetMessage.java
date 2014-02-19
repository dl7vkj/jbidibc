package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

public class VendorGetMessage extends BidibCommandMessage {
    public VendorGetMessage(String name) {
        super(0, BidibLibrary.MSG_VENDOR_GET, ByteUtils.bstr(name));
    }

    public VendorGetMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return ByteUtils.cstr(getData(), 0);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { VendorResponse.TYPE };
    }
}
