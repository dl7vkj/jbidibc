package org.bidib.jbidibc.enumeration;

public enum PomOperation implements BidibEnum {
    // @formatter:off
    RD_BLOCK(0, "read-block"), RD_BYTE(1, "read-byte"), RD_BIT(1, "read-bit"), WR_BIT(2, "write-bit"), WR_BYTE(3,
        "write-byte");
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
