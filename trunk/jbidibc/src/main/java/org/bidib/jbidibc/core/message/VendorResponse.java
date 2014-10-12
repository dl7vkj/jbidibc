package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.VendorData;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;

public class VendorResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_VENDOR;

    VendorResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length == 0) {
            throw new ProtocolException("no vendor data received");
        }
    }

    public VendorResponse(byte[] addr, int num, String name, String value) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_VENDOR, ByteUtils.bstr(name, value));
    }

    public String getName() {
        return "MSG_VENDOR";
    }

    public VendorData getVendorData() {
        VendorData result = new VendorData();
        byte[] data = getData();

        result.setName(ByteUtils.cstr(data, 0));
        result.setValue(ByteUtils.cstr(data, result.getName().length() + 1));
        return result;
    }
}
