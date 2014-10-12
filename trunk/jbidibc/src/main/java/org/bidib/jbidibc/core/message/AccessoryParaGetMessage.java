package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;

public class AccessoryParaGetMessage extends BidibCommandMessage {
    public AccessoryParaGetMessage(int accessoryNumber, int paraNumber) {
        super(0, BidibLibrary.MSG_ACCESSORY_PARA_GET, new byte[] { ByteUtils.getLowByte(accessoryNumber),
            ByteUtils.getLowByte(paraNumber) });
    }

    public AccessoryParaGetMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_ACCESSORY_PARA_GET";
    }

    public int getAccessoryNumber() {
        return ByteUtils.getInt(getData()[0]);
    }

    public int getParaNumber() {
        return ByteUtils.getInt(getData()[1]);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { AccessoryParaResponse.TYPE };
    }
}
