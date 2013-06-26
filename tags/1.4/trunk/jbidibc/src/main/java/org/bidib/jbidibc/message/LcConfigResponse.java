package org.bidib.jbidibc.message;

import org.bidib.jbidibc.LcConfig;
import org.bidib.jbidibc.enumeration.LcOutputType;
import org.bidib.jbidibc.exception.ProtocolException;

public class LcConfigResponse extends BidibMessage {
    LcConfigResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 6) {
            throw new ProtocolException("no lc config received");
        }
    }

    public LcConfig getLcConfig() {
        byte[] data = getData();

        return new LcConfig(LcOutputType.valueOf(data[0]), data[1] & 0xFF, data[2] & 0xFF, data[3] & 0xFF,
            data[4] & 0xFF, data[5] & 0xFF);
    }
}
