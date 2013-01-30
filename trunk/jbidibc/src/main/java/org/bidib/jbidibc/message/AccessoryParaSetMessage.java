package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.utils.ByteUtils;

public class AccessoryParaSetMessage extends BidibMessage {
    public AccessoryParaSetMessage(int accessoryNumber, int parameter, byte[] value) {
        super(0, BidibLibrary.MSG_ACCESSORY_PARA_SET, ByteUtils.concat(new byte[] { (byte) accessoryNumber, (byte) parameter },
                value));
    }
}
