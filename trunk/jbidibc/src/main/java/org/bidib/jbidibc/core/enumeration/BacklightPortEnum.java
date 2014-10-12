package org.bidib.jbidibc.core.enumeration;

public enum BacklightPortEnum implements BidibEnum {
    START(0);

    private final byte type;

    BacklightPortEnum(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    /**
     * Create a backlight port enum.
     * 
     * @param type
     *            numeric value of the backlight port enum
     * 
     * @return BacklightPortEnum
     */
    public static BacklightPortEnum valueOf(byte type) {
        BacklightPortEnum result = null;

        for (BacklightPortEnum e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a backlight port enum");
        }
        return result;
    }
}
