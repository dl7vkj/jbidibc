package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.FirmwareUpdateStat;
import org.bidib.jbidibc.core.enumeration.FirmwareUpdateState;
import org.bidib.jbidibc.core.exception.ProtocolException;

public class FwUpdateStatResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_FW_UPDATE_STAT;

    FwUpdateStatResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 2) {
            throw new ProtocolException("no firmware update status received");
        }
    }

    public FwUpdateStatResponse(byte[] addr, int num, byte status, byte timeout) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_FW_UPDATE_STAT, status, timeout);
    }

    public String getName() {
        return "MSG_FW_UPDATE_STAT";
    }

    public FirmwareUpdateStat getUpdateStat() {
        byte[] data = getData();

        return new FirmwareUpdateStat(FirmwareUpdateState.valueOf(data[0]), data[1]);
    }
}
