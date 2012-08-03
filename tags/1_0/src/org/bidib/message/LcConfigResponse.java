package org.bidib.message;

import org.bidib.LcConfig;
import org.bidib.enumeration.LcOutputType;
import org.bidib.exception.ProtocolException;

public class LcConfigResponse extends BidibMessage {
    LcConfigResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 6) {
            throw new ProtocolException("no lc config received");
        }
    }

    public LcConfig getLcConfig() {
        byte[] data = getData();

        return new LcConfig(LcOutputType.valueOf(data[0]), data[1], data[2], data[3], data[4], data[5]);
    }
}
