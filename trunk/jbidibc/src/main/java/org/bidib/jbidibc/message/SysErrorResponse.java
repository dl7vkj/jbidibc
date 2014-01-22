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

        int errorCode = getErrorCode();

        switch (errorCode) {
            case 0x00:
                LOGGER.warn("Received SysErrorResponse, BIDIB_ERR_NONE for address: {}, errorCode: {} (0x{})", addr,
                    errorCode, String.format("%02x", errorCode));
                break;
            case 0x01:
                LOGGER
                    .warn(
                        "Received SysErrorResponse, BIDIB_ERR_TXT for address: {}, errorCode: {} (0x{}), text len: {}, text: {}",
                        addr, errorCode, String.format("%02x", errorCode), ByteUtils.getInt(getData()[1]), ByteUtils
                            .cstr(data, 1));
                break;
            case 0x02:
                LOGGER
                    .warn(
                        "Received SysErrorResponse, BIDIB_ERR_CRC for address: {}, errorCode: {} (0x{}), sequence num of invalid message: {}",
                        addr, errorCode, String.format("%02x", errorCode), ByteUtils.getInt(getData()[1]));
                break;
            case 0x03:
                LOGGER
                    .warn(
                        "Received SysErrorResponse, BIDIB_ERR_SIZE for address: {}, errorCode: {} (0x{}), sequence num of invalid message: {}",
                        addr, errorCode, String.format("%02x", errorCode), ByteUtils.getInt(getData()[1]));
                break;
            case 0x04:
                LOGGER
                    .warn(
                        "Received SysErrorResponse, BIDIB_ERR_SEQUENCE for address: {}, errorCode: {} (0x{}), last correct sequence: {}",
                        addr, errorCode, String.format("%02x", errorCode), ByteUtils.getInt(getData()[1]));
                break;
            case 0x05:
                LOGGER
                    .warn(
                        "Received SysErrorResponse, BIDIB_ERR_PARAMETER for address: {}, errorCode: {} (0x{}), sequence num of invalid message: {}",
                        addr, errorCode, String.format("%02x", errorCode), ByteUtils.getInt(getData()[1]));
                break;
            case 0x10:
                LOGGER.warn(
                    "Received SysErrorResponse, BIDIB_ERR_BUS for address: {}, errorCode: {} (0x{}), error number: {}",
                    addr, errorCode, String.format("%02x", errorCode), ByteUtils.getInt(getData()[1]));
                break;
            case 0x11:
                LOGGER.warn(
                    "Received SysErrorResponse, BIDIB_ERR_ADDRSTACK for address: {}, errorCode: {} (0x{}), data: {}",
                    addr, errorCode, String.format("%02x", errorCode), ByteUtils.toString(data));
                break;
            case 0x12:
                LOGGER.warn("Received SysErrorResponse, BIDIB_ERR_IDDOUBLE for address: {}, errorCode: {} (0x{})",
                    addr, errorCode, String.format("%02x", errorCode));
                break;
            case 0x13:
                LOGGER
                    .warn(
                        "Received SysErrorResponse, BIDIB_ERR_SUBCRC for address: {}, errorCode: {} (0x{}), local node addr: {}",
                        addr, errorCode, String.format("%02x", errorCode), ByteUtils.getInt(getData()[1]));
                break;
            case 0x14:
                LOGGER
                    .warn(
                        "Received SysErrorResponse, BIDIB_ERR_SUBTIME for address: {}, errorCode: {} (0x{}), local node addr: {}",
                        addr, errorCode, String.format("%02x", errorCode), ByteUtils.getInt(getData()[1]));
                break;
            case 0x15:
                LOGGER
                    .warn(
                        "Received SysErrorResponse, BIDIB_ERR_SUBPAKET for address: {}, errorCode: {} (0x{}), local node addr: {}",
                        addr, errorCode, String.format("%02x", errorCode), ByteUtils.getInt(getData()[1]));
                break;
            case 0x16:
                LOGGER.warn("Received SysErrorResponse, BIDIB_ERR_OVERRUN for address: {}, errorCode: {} (0x{})", addr,
                    errorCode, String.format("%02x", errorCode));
                break;
            case 0x20:
                LOGGER
                    .warn(
                        "Received SysErrorResponse, BIDIB_ERR_HW for address: {}, errorCode: {} (0x{}), manufacturer specific error number: {}",
                        addr, errorCode, String.format("%02x", errorCode), ByteUtils.getInt(getData()[1]));
                break;
            default:
                LOGGER.warn("Received SysErrorResponse for address: {}, errorCode: {} (0x{})", addr, errorCode, String
                    .format("%02x", errorCode));
                break;
        }
    }

    public int getErrorCode() {
        return ByteUtils.getInt(getData()[0]);
    }
}
