package org.bidib.jbidibc.enumeration;

import org.bidib.jbidibc.BidibLibrary;

public enum SwitchPortEnum implements BidibEnum {
    // @formatter:off
    ON(BidibLibrary.BIDIB_PORT_TURN_ON), OFF(BidibLibrary.BIDIB_PORT_TURN_OFF);
    // @formatter:on

    private final byte type;

    SwitchPortEnum(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    /**
     * Create a switch port enum.
     * 
     * @param type
     *            numeric value of the switch port enum
     * 
     * @return SwitchPortEnum
     */
    public static SwitchPortEnum valueOf(byte type) {
        SwitchPortEnum result = null;

        for (SwitchPortEnum e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a switch port enum");
        }
        return result;
    }
}
