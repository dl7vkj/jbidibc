package org.bidib.message;

import org.bidib.FirmwareUpdateStat;
import org.bidib.enumeration.FirmwareUpdateState;
import org.bidib.exception.ProtocolException;

public class FwUpdateStatResponse extends BidibMessage {
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
