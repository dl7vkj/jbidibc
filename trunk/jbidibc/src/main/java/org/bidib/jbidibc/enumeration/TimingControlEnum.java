package org.bidib.jbidibc.enumeration;

public enum TimingControlEnum implements BidibEnum {
    // @formatter:off
    COIL_ON_OFF(0), OUTPUT_UNIT(1);
    // @formatter:on

    private final byte type;

    TimingControlEnum(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    /**
     * Create a timing control enum.
     * 
     * @param type
     *            numeric value of the timing control enum
     * 
     * @return TimingControlEnum
     */
    public static TimingControlEnum valueOf(byte type) {
        TimingControlEnum result = null;

        for (TimingControlEnum e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a timing control enum");
        }
        return result;
    }
}
