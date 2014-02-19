package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

public class AccessoryParaSetMessage extends BidibMessage implements BidibCommand {
    public AccessoryParaSetMessage(int accessoryNumber, int parameter, byte[] value) {
        super(0, BidibLibrary.MSG_ACCESSORY_PARA_SET, ByteUtils.concat(new byte[] { (byte) accessoryNumber,
            (byte) parameter }, value));
    }

    public AccessoryParaSetMessage(byte[] message) throws ProtocolException {
        super(message);
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
        return null;
    }
}
