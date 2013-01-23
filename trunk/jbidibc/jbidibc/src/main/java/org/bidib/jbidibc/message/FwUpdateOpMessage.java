package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.FirmwareUpdateOperation;

public class FwUpdateOpMessage extends BidibMessage {
    public FwUpdateOpMessage(FirmwareUpdateOperation operation, byte... data) {
        super(0, BidibLibrary.MSG_FW_UPDATE_OP, concat(new byte[] { operation.getType() }, data));
    }
}
