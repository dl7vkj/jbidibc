package org.bidib.jbidibc.enumeration;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.utils.ByteUtils;

public enum SysErrorEnum implements BidibEnum {
    BIDIB_ERR_NONE(BidibLibrary.BIDIB_ERR_NONE), BIDIB_ERR_TXT(BidibLibrary.BIDIB_ERR_TXT), BIDIB_ERR_CRC(
        BidibLibrary.BIDIB_ERR_CRC), BIDIB_ERR_SIZE(BidibLibrary.BIDIB_ERR_SIZE), BIDIB_ERR_SEQUENCE(
        BidibLibrary.BIDIB_ERR_SEQUENCE), BIDIB_ERR_PARAMETER(BidibLibrary.BIDIB_ERR_PARAMETER), BIDIB_ERR_BUS(
        BidibLibrary.BIDIB_ERR_BUS), BIDIB_ERR_ADDRSTACK(BidibLibrary.BIDIB_ERR_ADDRSTACK), BIDIB_ERR_IDDOUBLE(
        BidibLibrary.BIDIB_ERR_IDDOUBLE), BIDIB_ERR_SUBCRC(BidibLibrary.BIDIB_ERR_SUBCRC), BIDIB_ERR_SUBTIME(
        BidibLibrary.BIDIB_ERR_SUBTIME), BIDIB_ERR_SUBPAKET(BidibLibrary.BIDIB_ERR_SUBPAKET), BIDIB_ERR_OVERRUN(
        BidibLibrary.BIDIB_ERR_OVERRUN), BIDIB_ERR_HW(BidibLibrary.BIDIB_ERR_HW), BIDIB_ERR_RESET_REQUIRED(
        BidibLibrary.BIDIB_ERR_RESET_REQUIRED);

    private final byte type;

    SysErrorEnum(int type) {
        this.type = ByteUtils.getLowByte(type);
    }

    @Override
    public byte getType() {
        return type;
    }

    /**
     * Create an SysErrorEnum from a byte value.
     * 
     * @param type
     *            numeric value of the sys error
     * 
     * @return SysErrorEnum
     */
    public static SysErrorEnum valueOf(byte type) {
        SysErrorEnum result = null;

        for (SysErrorEnum e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("Cannot map " + type + " to an sys error.");
        }
        return result;
    }

}
