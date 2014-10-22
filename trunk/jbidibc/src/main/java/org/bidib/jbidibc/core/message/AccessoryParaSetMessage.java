package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;

public class AccessoryParaSetMessage extends BidibCommandMessage {
    public static final Integer TYPE = BidibLibrary.MSG_ACCESSORY_PARA_SET;

    public AccessoryParaSetMessage(int accessoryNumber, int parameter, byte[] value) {
        super(0, BidibLibrary.MSG_ACCESSORY_PARA_SET, ByteUtils.concat(new byte[] { (byte) accessoryNumber,
            (byte) parameter }, value));
    }

    public AccessoryParaSetMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_ACCESSORY_PARA_SET";
    }

    public int getAccessoryNumber() {
        return ByteUtils.getInt(getData()[0]);
    }

    public int getParaNumber() {
        return ByteUtils.getInt(getData()[1]);
    }

    public byte[] getValue() {
        byte[] data = getData();
        byte[] result = new byte[data.length - 2];

        System.arraycopy(data, 2, result, 0, result.length);
        return result;
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { AccessoryParaResponse.TYPE };
    }
}
