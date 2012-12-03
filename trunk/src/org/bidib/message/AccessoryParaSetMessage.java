package org.bidib.message;

import org.bidib.AccessoryState;
import org.bidib.BidibLibrary;

public class AccessoryParaSetMessage extends BidibMessage {
    public AccessoryParaSetMessage(AccessoryState accessoryState) {
        super(0, BidibLibrary.MSG_ACCESSORY_PARA_SET, new byte[] { (byte) accessoryState.getAccessoryNumber(),
                (byte) accessoryState.getAspect(), (byte) accessoryState.getTotal(),
                (byte) accessoryState.getExecute(), (byte) accessoryState.getWait() });
    }
}
