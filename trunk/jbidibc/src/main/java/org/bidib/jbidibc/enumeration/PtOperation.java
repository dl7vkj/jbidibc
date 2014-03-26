package org.bidib.jbidibc.enumeration;

/**
 * The operations supported by the programming track.
 */
public enum PtOperation implements BidibEnum {
    RD_BYTE(2, "read-byte"), WR_BYTE(4, "write-byte"), RD_BIT(3, "read-bit"), WR_BIT(3, "write-bit");

    private final byte type;

    private final String key;

    private PtOperation(int type, String key) {
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
