package org.bidib.jbidibc.core.enumeration;

import org.bidib.jbidibc.core.BidibLibrary;

public enum PomOperation implements BidibEnum {
    // @formatter:off
    RD_BLOCK(0, "read-block"), RD_BYTE(1, "read-byte"), RD_BIT(1, "read-bit"), WR_BIT(2, "write-bit"), WR_BYTE(3,
        "write-byte"), X_RD_BLOCK(BidibLibrary.BIDIB_CS_xPOM_RD_BLOCK, "x-read-block"), X_WR_BIT(
        BidibLibrary.BIDIB_CS_xPOM_WR_BIT, "x-write-bit"), X_WR_BYTE1(BidibLibrary.BIDIB_CS_xPOM_WR_BYTE1,
        "x-write-byte1"), X_WR_BYTE2(BidibLibrary.BIDIB_CS_xPOM_WR_BYTE2, "x-write-byte2"), X_WR_BYTE3(
        BidibLibrary.BIDIB_CS_xPOM_WR_BYTE3, "x-write-byte3"), X_WR_BYTE4(BidibLibrary.BIDIB_CS_xPOM_WR_BYTE4,
        "x-write-byte4");
    // @formatter:on

    // NOTE: POM does not support READ-BIT but we use it as operation. In this case the OPCODE of READ-BYTE
    // is used and the requested bit value must be extracted from the returned byte value.

    private final byte type;

    private final String key;

    private PomOperation(int type, String key) {
        this.type = (byte) type;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public byte getType() {
        return type;
    }
}
