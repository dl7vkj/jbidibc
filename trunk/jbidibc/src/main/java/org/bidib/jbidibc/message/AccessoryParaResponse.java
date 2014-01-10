package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

public class AccessoryParaResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_ACCESSORY_PARA;

    AccessoryParaResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length < 2) {
            throw new ProtocolException("no accessory parameter received");
        }
    }

    public AccessoryParaResponse(byte[] addr, int num, byte accessoryNum, byte paraNum, byte[] value)
        throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_ACCESSORY_PARA, ByteUtils.concat(new byte[] { accessoryNum, paraNum }, value));
    }

    public int getAccessoryNumber() {
        return getData()[0];
    }

    public int getParameter() {
        return getData()[1];
    }

    public byte[] getValue() {
        byte[] data = getData();
        byte[] result = new byte[data.length - 2];

        System.arraycopy(data, 2, result, 0, result.length);
        return result;
    }
}
