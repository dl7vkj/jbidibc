package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;

public class AccessoryParaSetMessage extends BidibMessage {
    public AccessoryParaSetMessage(int accessoryNumber, int parameter, byte[] value) {
        super(0, BidibLibrary.MSG_ACCESSORY_PARA_SET, concat(new byte[] { (byte) accessoryNumber, (byte) parameter },
                value));
    }
}
