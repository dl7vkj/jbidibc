package org.bidib.jbidibc.core.enumeration;

public enum TimeBaseUnitEnum implements BidibEnum {
    // @formatter:off
    UNIT_100MS("unit-100ms", 0), UNIT_1S("unit-1s", 1);
    // @formatter:on

    private final String key;

    private final byte type;

    TimeBaseUnitEnum(String key, int type) {
        this.key = key;
        this.type = (byte) type;
    }

    public String getKey() {
        return key;
    }

    public byte getType() {
        return type;
    }

    /**
     * Create a time base unit enum.
     * 
     * @param type
     *            numeric value of the time base unit enum
     * 
     * @return TimeBaseUnitEnum
     */
    public static TimeBaseUnitEnum valueOf(byte type) {
        TimeBaseUnitEnum result = null;

        for (TimeBaseUnitEnum e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a time base unit enum");
        }
        return result;
    }
}
