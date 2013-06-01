package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SysErrorResponse extends BidibMessage {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysErrorResponse.class);

    public static final Integer TYPE = BidibLibrary.MSG_SYS_ERROR;

    SysErrorResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length < 1) {
            throw new ProtocolException("no error received");
        }

        LOGGER.warn("Received SysErrorResponse for address: {}, errorCode: {} (0x{})", new Object[]{addr, getErrorCode(), String.format("%02x", getErrorCode())});
    }

    public int getErrorCode() {
        return ByteUtils.getInt(getData()[0]);
    }
}
