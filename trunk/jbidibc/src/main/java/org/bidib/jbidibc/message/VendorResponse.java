package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.VendorData;
import org.bidib.jbidibc.exception.ProtocolException;

public class VendorResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_VENDOR;

    VendorResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length == 0) {
            throw new ProtocolException("no vendor data received");
        }
    }

    public VendorData getVendorData() {
        VendorData result = new VendorData();
        byte[] data = getData();

        result.setName(cstr(data, 0));
        result.setValue(cstr(data, result.getName().length() + 1));
        return result;
    }

    private static String cstr(byte[] bstr, int offset) {
        int length = bstr[offset];
        byte[] cstr = new byte[length];

        System.arraycopy(bstr, offset + 1, cstr, 0, length);
        return new String(cstr);
    }
}
