package org.bidib.enumeration;

public enum MacroEnum implements BidibEnum {
    // @formatter:off
    START (0),
    STOP  (1),
    END   (2);
    // @formatter:on
    private final byte type;

    MacroEnum(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    /**
     * Create a macro enum.
     * 
     * @param type
     *            numeric value of the macro enum
     * 
     * @return MacroEnum
     */
    public static MacroEnum valueOf(byte type) {
        MacroEnum result = null;

        for (MacroEnum e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a macro enum");
        }
        return result;
    }
}
