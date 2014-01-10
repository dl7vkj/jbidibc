package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

public class AccessoryParaGetMessage extends BidibMessage {
    public AccessoryParaGetMessage(int accessoryNumber, int paraNumber) {
        super(0, BidibLibrary.MSG_ACCESSORY_PARA_GET, new byte[] { ByteUtils.getLowByte(accessoryNumber),
            ByteUtils.getLowByte(paraNumber) });
    }

    public AccessoryParaGetMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public int getAccessoryNumber() {
        return ByteUtils.getInt(getData()[0]);
    }

    public int getParaNumber() {
        return ByteUtils.getInt(getData()[1]);
    }
}
