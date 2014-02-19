package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

public class AccessorySetMessage extends BidibMessage implements BidibCommand {
    public AccessorySetMessage(int accessoryNumber, int aspect) {
        super(0, BidibLibrary.MSG_ACCESSORY_SET, new byte[] { (byte) accessoryNumber, (byte) aspect });
    }

    public AccessorySetMessage(byte[] message) throws ProtocolException {
        super(message);
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
