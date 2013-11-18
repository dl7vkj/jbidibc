package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.FirmwareUpdateStat;
import org.bidib.jbidibc.enumeration.FirmwareUpdateState;
import org.bidib.jbidibc.exception.ProtocolException;

public class FwUpdateStatResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_FW_UPDATE_STAT;

    FwUpdateStatResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 2) {
            throw new ProtocolException("no firmware update status received");
        }
    }

    public FirmwareUpdateStat getUpdateStat() {
        byte[] data = getData();

        return new FirmwareUpdateStat(FirmwareUpdateState.valueOf(data[0]), data[1]);
    }
}
