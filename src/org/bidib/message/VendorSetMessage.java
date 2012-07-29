package org.bidib.message;

import org.bidib.BidibLibrary;

public class VendorSetMessage extends BidibMessage {
    public VendorSetMessage(String name, String value) {
        super(0, BidibLibrary.MSG_VENDOR_SET, bstr(name, value));
    }

    private static byte[] bstr(String name, String value) {
        byte[] result = new byte[name.length() + value.length() + 2];

        result[0] = (byte) (name.length());
        System.arraycopy(name.getBytes(), 0, result, 1, name.length());
        result[name.length() + 1] = (byte) (value.length());
        System.arraycopy(value.getBytes(), 0, result, name.length() + 2, value.length());
        return result;
    }
}
