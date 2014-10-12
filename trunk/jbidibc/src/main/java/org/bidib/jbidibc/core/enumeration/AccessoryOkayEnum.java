package org.bidib.jbidibc.core.enumeration;

public enum AccessoryOkayEnum implements BidibEnum {
    // @formatter:off
    NO_FEEDBACK(0), QUERY0(1), QUERY1(2);
    // @formatter:on

    private final byte type;

    AccessoryOkayEnum(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    /**
     * Create a accessory okay enum.
     * 
     * @param type
     *            numeric value of the accessory okay enum
     * 
     * @return InputEnum
     */
    public static AccessoryOkayEnum valueOf(byte type) {
        AccessoryOkayEnum result = null;

        for (AccessoryOkayEnum e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a accessory okay enum");
        }
        return result;
    }
}
