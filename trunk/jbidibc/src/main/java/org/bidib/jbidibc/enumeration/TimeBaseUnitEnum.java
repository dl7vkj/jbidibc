package org.bidib.jbidibc.enumeration;

public enum TimeBaseUnitEnum implements BidibEnum {
    // @formatter:off
    UNIT_100MS(0), UNIT_1S(1);
    // @formatter:on

    private final byte type;

    TimeBaseUnitEnum(int type) {
        this.type = (byte) type;
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
