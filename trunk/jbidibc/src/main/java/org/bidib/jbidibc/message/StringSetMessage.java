package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.utils.ByteUtils;

public class StringSetMessage extends BidibMessage {
    public StringSetMessage(int namespace, int stringId, String value) {
        super(0, BidibLibrary.MSG_STRING_SET, bstr(namespace, stringId, value));
    }

    private static byte[] bstr(int namespace, int stringId, String value) {
        if (value.length() > 127) {
            throw new IllegalArgumentException("Length of string exceeds 127.");
        }

        byte[] result = new byte[3 + value.length()];

        result[0] = ByteUtils.getLowByte(namespace);
        result[1] = ByteUtils.getLowByte(stringId);
        result[2] = (byte) (value.length());
        System.arraycopy(value.getBytes(), 0, result, 3, value.length());
        return result;
    }
}
