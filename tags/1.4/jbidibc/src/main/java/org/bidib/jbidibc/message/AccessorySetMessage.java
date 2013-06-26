package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class AccessorySetMessage extends BidibMessage {
    public AccessorySetMessage(int accessoryNumber, int aspect) {
        super(0, BidibLibrary.MSG_ACCESSORY_SET, new byte[] { (byte) accessoryNumber, (byte) aspect });
    }
}
