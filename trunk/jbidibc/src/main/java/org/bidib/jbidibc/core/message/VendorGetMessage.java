package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;

public class VendorGetMessage extends BidibCommandMessage {
    protected VendorGetMessage(String name) {
        super(0, BidibLibrary.MSG_VENDOR_GET, ByteUtils.bstr(name));
    }

    public VendorGetMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_VENDOR_GET";
    }

    public String getVendorDataName() {
        return ByteUtils.cstr(getData(), 0);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { VendorResponse.TYPE };
    }
}
