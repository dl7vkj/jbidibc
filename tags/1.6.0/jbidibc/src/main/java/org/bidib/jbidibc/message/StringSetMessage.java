package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.StringData;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

public class StringSetMessage extends BidibCommandMessage {
    public StringSetMessage(int namespace, int stringId, String value) {
        super(0, BidibLibrary.MSG_STRING_SET, bstr(namespace, stringId, value));
    }

    public StringSetMessage(byte[] message) throws ProtocolException {
        super(message);
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

    public int getNamespace() {
        return ByteUtils.getInt(getData()[0]);
    }

    public int getStringId() {
        return ByteUtils.getInt(getData()[1]);
    }

    public String getString() {
        return ByteUtils.cstr(getData(), 2);
    }

    public StringData getStringData() {
        StringData stringData = new StringData();
        byte[] data = getData();

        stringData.setNamespace(ByteUtils.getInt(data[0]));
        stringData.setIndex(ByteUtils.getInt(data[1]));
        stringData.setValue(ByteUtils.cstr(data, 2));
        return stringData;
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { StringResponse.TYPE };
    }
}
