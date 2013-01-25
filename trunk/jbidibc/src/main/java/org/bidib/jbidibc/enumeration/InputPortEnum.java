package org.bidib.jbidibc.enumeration;

public enum InputPortEnum implements BidibEnum {
    // @formatter:off
    OFF (0),
    ON  (1);
    // @formatter:on

    private final byte type;

    InputPortEnum(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    /**
     * Create an input port enum.
     * 
     * @param type
     *            numeric value of the input port enum
     * 
     * @return InputPortEnum
     */
    public static InputPortEnum valueOf(byte type) {
        InputPortEnum result = null;

        for (InputPortEnum e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to an input port enum");
        }
        return result;
    }
}
