package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;

public class AccessoryGetMessage extends BidibCommandMessage {
    public AccessoryGetMessage(int accessoryNumber) {
        super(0, BidibLibrary.MSG_ACCESSORY_GET, new byte[] { (byte) accessoryNumber });
    }

    public AccessoryGetMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_ACCESSORY_GET";
    }

    public int getAccessoryNumber() {
        return ByteUtils.getInt(getData()[0]);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
