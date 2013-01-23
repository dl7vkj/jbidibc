package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.FirmwareUpdateOperation;

public class FwUpdateOpMessage extends BidibMessage {
    public FwUpdateOpMessage(FirmwareUpdateOperation operation, byte... data) {
        super(0, BidibLibrary.MSG_FW_UPDATE_OP, join(operation.getType(), data));
    }

    private static byte[] join(byte b, byte[] bytes) {
        byte[] result = new byte[bytes != null ? bytes.length + 1 : 1];

        result[0] = b;
        if (bytes != null) {
            System.arraycopy(bytes, 0, result, 1, bytes.length);
        }
        return result;
    }
}
