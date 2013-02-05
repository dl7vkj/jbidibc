package org.bidib.jbidibc.message;

import org.bidib.jbidibc.exception.ProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SysErrorResponse extends BidibMessage {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysErrorResponse.class);

    SysErrorResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length < 1) {
            throw new ProtocolException("no error received");
        }

        LOGGER.warn("Received SysErrorResponse for address: {}, errorCode: {}", addr, getErrorCode());
    }

    public int getErrorCode() {
        return getData()[0];
    }
}
