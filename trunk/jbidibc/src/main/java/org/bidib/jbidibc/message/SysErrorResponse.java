package org.bidib.jbidibc.message;

import java.util.Arrays;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.SysErrorEnum;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SysErrorResponse extends BidibMessage {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysErrorResponse.class);

    public static final Integer TYPE = BidibLibrary.MSG_SYS_ERROR;

    private byte[] reasonData;

    public SysErrorResponse(byte[] addr, int num, SysErrorEnum sysError, byte... reason) throws ProtocolException {
        this(addr, num, TYPE, ByteUtils.concat(new byte[] { sysError.getType() }, reason));
    }

    SysErrorResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length < 1) {
            throw new ProtocolException("no error received");
        }

        try {
            SysErrorEnum sysErrorEnum = SysErrorEnum.valueOf(getData()[0]);

            switch (sysErrorEnum) {
                case BIDIB_ERR_NONE:
                    LOGGER.warn("Received SysErrorResponse, BIDIB_ERR_NONE for address: {}, errorCode: {} (0x{})",
                        addr, sysErrorEnum.getType(), String.format("%02x", sysErrorEnum.getType()));
                    break;
                case BIDIB_ERR_TXT:
                    LOGGER
                        .warn(
                            "Received SysErrorResponse, BIDIB_ERR_TXT for address: {}, errorCode: {} (0x{}), text len: {}, text: {}",
                            addr, sysErrorEnum.getType(), String.format("%02x", sysErrorEnum.getType()),
                            ByteUtils.getInt(getData()[1]), ByteUtils.cstr(data, 1));

                    reasonData = ByteUtils.cstr(data, 1).getBytes();
                    break;
                case BIDIB_ERR_CRC:
                    LOGGER
                        .warn(
                            "Received SysErrorResponse, BIDIB_ERR_CRC for address: {}, errorCode: {} (0x{}), sequence num of invalid message: {}",
                            addr, sysErrorEnum.getType(), String.format("%02x", sysErrorEnum.getType()),
                            ByteUtils.getInt(getData()[1]));
                    reasonData = new byte[] { getData()[1] };
                    break;
                case BIDIB_ERR_SIZE:
                    LOGGER
                        .warn(
                            "Received SysErrorResponse, BIDIB_ERR_SIZE for address: {}, errorCode: {} (0x{}), sequence num of invalid message: {}",
                            addr, sysErrorEnum.getType(), String.format("%02x", sysErrorEnum.getType()),
                            ByteUtils.getInt(getData()[1]));
                    reasonData = new byte[] { getData()[1] };
                    break;
                case BIDIB_ERR_SEQUENCE:
                    LOGGER
                        .warn(
                            "Received SysErrorResponse, BIDIB_ERR_SEQUENCE for address: {}, errorCode: {} (0x{}), last correct sequence: {}",
                            addr, sysErrorEnum.getType(), String.format("%02x", sysErrorEnum.getType()),
                            ByteUtils.getInt(getData()[1]));
                    reasonData = new byte[] { getData()[1] };
                    break;
                case BIDIB_ERR_PARAMETER:
                    LOGGER
                        .warn(
                            "Received SysErrorResponse, BIDIB_ERR_PARAMETER for address: {}, errorCode: {} (0x{}), sequence num of invalid message: {}",
                            addr, sysErrorEnum.getType(), String.format("%02x", sysErrorEnum.getType()),
                            ByteUtils.getInt(getData()[1]));
                    reasonData = new byte[] { getData()[1] };
                    break;
                case BIDIB_ERR_BUS:
                    LOGGER
                        .warn(
                            "Received SysErrorResponse, BIDIB_ERR_BUS for address: {}, errorCode: {} (0x{}), error number: {}",
                            addr, sysErrorEnum.getType(), String.format("%02x", sysErrorEnum.getType()),
                            ByteUtils.getInt(getData()[1]));
                    reasonData = new byte[] { getData()[1] };
                    break;
                case BIDIB_ERR_ADDRSTACK:
                    LOGGER
                        .warn(
                            "Received SysErrorResponse, BIDIB_ERR_ADDRSTACK for address: {}, errorCode: {} (0x{}), data: {}",
                            addr, sysErrorEnum.getType(), String.format("%02x", sysErrorEnum.getType()),
                            ByteUtils.toString(data));
                    reasonData = Arrays.copyOfRange(data, 1, data.length);
                    break;
                case BIDIB_ERR_IDDOUBLE:
                    LOGGER.warn("Received SysErrorResponse, BIDIB_ERR_IDDOUBLE for address: {}, errorCode: {} (0x{})",
                        addr, sysErrorEnum.getType(), String.format("%02x", sysErrorEnum.getType()));
                    break;
                case BIDIB_ERR_SUBCRC:
                    LOGGER
                        .warn(
                            "Received SysErrorResponse, BIDIB_ERR_SUBCRC for address: {}, errorCode: {} (0x{}), local node addr: {}",
                            addr, sysErrorEnum.getType(), String.format("%02x", sysErrorEnum.getType()),
                            ByteUtils.getInt(getData()[1]));
                    reasonData = new byte[] { getData()[1] };
                    break;
                case BIDIB_ERR_SUBTIME:
                    LOGGER
                        .warn(
                            "Received SysErrorResponse, BIDIB_ERR_SUBTIME for address: {}, errorCode: {} (0x{}), local node addr: {}",
                            addr, sysErrorEnum.getType(), String.format("%02x", sysErrorEnum.getType()),
                            ByteUtils.getInt(getData()[1]));
                    reasonData = new byte[] { getData()[1] };
                    break;
                case BIDIB_ERR_SUBPAKET:
                    LOGGER
                        .warn(
                            "Received SysErrorResponse, BIDIB_ERR_SUBPAKET for address: {}, errorCode: {} (0x{}), local node addr: {}",
                            addr, sysErrorEnum.getType(), String.format("%02x", sysErrorEnum.getType()),
                            ByteUtils.getInt(getData()[1]));
                    reasonData = new byte[] { getData()[1] };
                    break;
                case BIDIB_ERR_OVERRUN:
                    LOGGER.warn("Received SysErrorResponse, BIDIB_ERR_OVERRUN for address: {}, errorCode: {} (0x{})",
                        addr, sysErrorEnum.getType(), String.format("%02x", sysErrorEnum.getType()));
                    break;
                case BIDIB_ERR_HW:
                    LOGGER
                        .warn(
                            "Received SysErrorResponse, BIDIB_ERR_HW for address: {}, errorCode: {} (0x{}), manufacturer specific error number: {}",
                            addr, sysErrorEnum.getType(), String.format("%02x", sysErrorEnum.getType()),
                            ByteUtils.getInt(getData()[1]));
                    reasonData = new byte[] { getData()[1] };
                    break;
                case BIDIB_ERR_RESET_REQUIRED:
                    LOGGER.warn(
                        "Received SysErrorResponse, BIDIB_ERR_RESET_REQUIRED for address: {}, errorCode: {} (0x{})",
                        addr, sysErrorEnum.getType(), String.format("%02x", sysErrorEnum.getType()));
                    break;
                default:
                    LOGGER.warn("Received SysErrorResponse for address: {}, errorCode: {} (0x{})", addr,
                        sysErrorEnum.getType(), String.format("%02x", sysErrorEnum.getType()));
                    break;
            }
        }
        catch (IllegalArgumentException ex) {
            int errorCode = getErrorCode();
            LOGGER.warn("Received SysErrorResponse for address: {}, errorCode: {} (0x{})", addr, errorCode,
                String.format("%02x", errorCode));
        }

    }

    public String getName() {
        return "MSG_SYS_ERROR";
    }

    public int getErrorCode() {
        return ByteUtils.getInt(getData()[0]);
    }

    public byte[] getReasonData() {
        return reasonData;
    }
}
