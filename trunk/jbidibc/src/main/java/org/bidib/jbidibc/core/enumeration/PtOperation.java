package org.bidib.jbidibc.core.enumeration;

/**
 * The operations supported by the programming track.
 */
public enum PtOperation implements BidibEnum {
    BREAK(0, "prog-break"), QUERY(1, "prog-query"), RD_BYTE(2, "prog-read-byte"), RD_BIT(3, "prog-read-bit"), WR_BIT(3,
        "prog-write-bit"), WR_BYTE(4, "prog-write-byte");

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
