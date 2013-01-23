package org.bidib.jbidibc.message;

import org.bidib.jbidibc.exception.ProtocolException;

public class AccessoryParaResponse extends BidibMessage {
    AccessoryParaResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length < 2) {
            throw new ProtocolException("no accessory parameter received");
        }
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
