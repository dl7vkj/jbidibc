package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;

public class AccessorySetMessage extends BidibCommandMessage {
    public AccessorySetMessage(int accessoryNumber, int aspect) {
        super(0, BidibLibrary.MSG_ACCESSORY_SET, new byte[] { (byte) accessoryNumber, (byte) aspect });
    }

    public AccessorySetMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_ACCESSORY_SET";
    }

    public int getAccessoryNumber() {
        return ByteUtils.getInt(getData()[0]);
    }

    public int getAspect() {
        return ByteUtils.getInt(getData()[1]);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
