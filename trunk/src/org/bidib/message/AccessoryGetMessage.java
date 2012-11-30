package org.bidib.message;

import org.bidib.BidibLibrary;

public class AccessoryGetMessage extends BidibMessage {
    public AccessoryGetMessage(int accessoryNumber) {
        super(0, BidibLibrary.MSG_ACCESSORY_GET, new byte[] { (byte) accessoryNumber });
    }
}
