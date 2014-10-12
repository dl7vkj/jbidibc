package org.bidib.jbidibc.core.enumeration;

public enum ActivateCoilEnum implements BidibEnum {
    // @formatter:off
    COIL_OFF(0), COIL_ON(1);
    // @formatter:on

    private final byte type;

    ActivateCoilEnum(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    /**
     * Create an activate coil enum.
     * 
     * @param type
     *            numeric value of the activate coil enum
     * 
     * @return ActivateCoilEnum
     */
    public static ActivateCoilEnum valueOf(byte type) {
        ActivateCoilEnum result = null;

        for (ActivateCoilEnum e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to an activate coil enum");
        }
        return result;
    }
}
