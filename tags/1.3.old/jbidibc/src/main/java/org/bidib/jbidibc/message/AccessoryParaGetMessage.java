package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class AccessoryParaGetMessage extends BidibMessage {
    public AccessoryParaGetMessage(int accessoryNumber, int parameter) {
        super(0, BidibLibrary.MSG_ACCESSORY_PARA_GET, new byte[] { (byte) accessoryNumber, (byte) parameter });
    }
}
