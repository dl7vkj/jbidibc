package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.StringData;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;

public class StringResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_STRING;

    StringResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length < 3) {
            throw new ProtocolException("no string data received");
        }
    }

    public StringResponse(byte[] addr, int num, byte namespace, byte index, String data) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_STRING, ByteUtils.concat(
            new byte[] { namespace, index, ByteUtils.getLowByte(data.length()) }, data.getBytes()));
    }

    public String getName() {
        return "MSG_STRING";
    }

    public StringData getStringData() {
        StringData stringData = new StringData();
        byte[] data = getData();

        stringData.setNamespace(ByteUtils.getInt(data[0]));
        stringData.setIndex(ByteUtils.getInt(data[1]));
        stringData.setValue(ByteUtils.cstr(data, 2));
        return stringData;
    }
}
