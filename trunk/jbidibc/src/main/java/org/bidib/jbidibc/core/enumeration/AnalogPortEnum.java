package org.bidib.jbidibc.core.enumeration;

public enum AnalogPortEnum implements BidibEnum {
    START(0);

    private final byte type;

    AnalogPortEnum(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    /**
     * Create an analog port enum.
     * 
     * @param type
     *            numeric value of the analog port enum
     * 
     * @return AnalogPortEnum
     */
    public static AnalogPortEnum valueOf(byte type) {
        AnalogPortEnum result = null;

        for (AnalogPortEnum e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a analog port enum");
        }
        return result;
    }
}
