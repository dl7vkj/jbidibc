package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class AccessoryGetMessage extends BidibMessage {
    public AccessoryGetMessage(int accessoryNumber) {
        super(0, BidibLibrary.MSG_ACCESSORY_GET, new byte[] { (byte) accessoryNumber });
    }
}
