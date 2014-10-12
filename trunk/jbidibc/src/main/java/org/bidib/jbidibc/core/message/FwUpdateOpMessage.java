package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.enumeration.FirmwareUpdateOperation;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;

public class FwUpdateOpMessage extends BidibCommandMessage {
    public FwUpdateOpMessage(FirmwareUpdateOperation operation, byte... data) {
        super(0, BidibLibrary.MSG_FW_UPDATE_OP, ByteUtils.concat(new byte[] { operation.getType() }, data));
    }

    public FwUpdateOpMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_FW_UPDATE_OP";
    }

    public FirmwareUpdateOperation getOperation() {
        return FirmwareUpdateOperation.valueOf(getData()[0]);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { FwUpdateStatResponse.TYPE };
    }
}
