package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.VendorData;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

public class VendorSetMessage extends BidibCommandMessage {
    public VendorSetMessage(String name, String value) {
        super(0, BidibLibrary.MSG_VENDOR_SET, ByteUtils.bstr(name, value));
    }

    public VendorSetMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_VENDOR_SET";
    }

    public VendorData getVendorData() {
        VendorData result = new VendorData();
        byte[] data = getData();

        result.setName(ByteUtils.cstr(data, 0));
        result.setValue(ByteUtils.cstr(data, result.getName().length() + 1));
        return result;
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { VendorResponse.TYPE };
    }
}
