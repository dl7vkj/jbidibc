package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.FirmwareUpdateOperation;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

public class FwUpdateOpMessage extends BidibCommandMessage {
    public FwUpdateOpMessage(FirmwareUpdateOperation operation, byte... data) {
        super(0, BidibLibrary.MSG_FW_UPDATE_OP, ByteUtils.concat(new byte[] { operation.getType() }, data));
    }

    public FwUpdateOpMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public FirmwareUpdateOperation getOperation() {
        return FirmwareUpdateOperation.valueOf(getData()[0]);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { FwUpdateStatResponse.TYPE };
    }
}
