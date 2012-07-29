package org.bidib.enumeration;

public enum CriticalFunctionEnum implements BidibEnum {
    // @formatter:off
    BEGIN (0),
    END   (1);
    // @formatter:on

    private final byte type;

    CriticalFunctionEnum(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    /**
     * Create a critical function enum.
     * 
     * @param type
     *            numeric value of the critical function enum
     * 
     * @return CriticalFunctionEnum
     */
    public static CriticalFunctionEnum valueOf(byte type) {
        CriticalFunctionEnum result = null;

        for (CriticalFunctionEnum e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a critical function enum");
        }
        return result;
    }
}
