package org.bidib.jbidibc.core.enumeration;

import org.bidib.jbidibc.core.BidibLibrary;

public enum LightPortEnum implements BidibEnum {
    // @formatter:off
    ON(BidibLibrary.BIDIB_PORT_TURN_ON), OFF(BidibLibrary.BIDIB_PORT_TURN_OFF), UP(BidibLibrary.BIDIB_PORT_DIMM_ON), DOWN(
        BidibLibrary.BIDIB_PORT_DIMM_OFF), NEON(BidibLibrary.BIDIB_PORT_TURN_ON_NEON), BLINKA(
        BidibLibrary.BIDIB_PORT_BLINK_A), BLINKB(BidibLibrary.BIDIB_PORT_BLINK_B), FLASHA(
        BidibLibrary.BIDIB_PORT_FLASH_A), FLASHB(BidibLibrary.BIDIB_PORT_FLASH_B), DOUBLEFLASH(
        BidibLibrary.BIDIB_PORT_DOUBLE_FLASH), UNKNOWN(255);
    // @formatter:on

    private final byte type;

    LightPortEnum(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    /**
     * Create a light port enum.
     * 
     * @param type
     *            numeric value of the light port enum
     * 
     * @return LightPortEnum
     */
    public static LightPortEnum valueOf(byte type) {
        LightPortEnum result = null;

        for (LightPortEnum e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a light port enum");
        }
        return result;
    }
}
