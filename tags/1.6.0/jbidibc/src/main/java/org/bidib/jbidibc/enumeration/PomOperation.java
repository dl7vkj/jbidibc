package org.bidib.jbidibc.enumeration;

public enum PomOperation implements BidibEnum {
    RD_BLOCK(0, "read-block"), RD_BYTE(1, "read-byte"), WR_BIT(2, "write-bit"), WR_BYTE(3, "write-byte");

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
