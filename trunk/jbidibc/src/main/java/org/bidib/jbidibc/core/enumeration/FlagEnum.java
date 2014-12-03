package org.bidib.jbidibc.core.enumeration;

public enum FlagEnum implements BidibEnum {
    // @formatter:off
    CLEAR(0), QUERY1(1), QUERY0(2), SET(3);
    // @formatter:on

    private final byte type;

    FlagEnum(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    /**
     * Create a flag enum.
     * 
     * @param type
     *            numeric value of the flag enum
     * 
     * @return FlagEnum
     */
    public static FlagEnum valueOf(byte type) {
        FlagEnum result = null;

        for (FlagEnum e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a flag enum");
        }
        return result;
    }
}
