package org.bidib.message;

import org.bidib.BidibLibrary;

public class VendorGetMessage extends BidibMessage {
    public VendorGetMessage(String name) {
        super(0, BidibLibrary.MSG_VENDOR_GET, bstr(name));
    }

    private static byte[] bstr(String cstr) {
        byte[] result = new byte[cstr.length() + 1];

        result[0] = (byte) (result.length - 1);
        System.arraycopy(cstr.getBytes(), 0, result, 1, result.length - 1);
        return result;
    }
}
