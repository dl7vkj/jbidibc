package org.bidib.jbidibc.enumeration;

public enum InputEnum implements BidibEnum {
    // @formatter:off
    QUERY0(0), QUERY1(1);
    // @formatter:on

    private final byte type;

    InputEnum(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    /**
     * Create a input enum.
     * 
     * @param type
     *            numeric value of the input enum
     * 
     * @return InputEnum
     */
    public static InputEnum valueOf(byte type) {
        InputEnum result = null;

        for (InputEnum e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a input enum");
        }
        return result;
    }
}
