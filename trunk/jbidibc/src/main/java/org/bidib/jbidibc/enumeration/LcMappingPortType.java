package org.bidib.jbidibc.enumeration;

import org.bidib.jbidibc.BidibLibrary;

public enum LcMappingPortType implements BidibEnum {
    // @formatter:off
    SWITCHPORT(BidibLibrary.BIDIB_OUTTYPE_SPORT), LIGHTPORT(BidibLibrary.BIDIB_OUTTYPE_LPORT), SERVOPORT(
        BidibLibrary.BIDIB_OUTTYPE_SERVO), SOUNDPORT(BidibLibrary.BIDIB_OUTTYPE_SOUND), MOTORPORT(
        BidibLibrary.BIDIB_OUTTYPE_MOTOR), ANALOGPORT(BidibLibrary.BIDIB_OUTTYPE_ANALOG), BACKLIGHTPORT(
        BidibLibrary.BIDIB_OUTTYPE_BACKLIGHT), INPUTPORT(BidibLibrary.BIDIB_PORTTYPE_INPUT);
    // @formatter:on

    private final byte type;

    LcMappingPortType(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    /**
     * Create a LcMappingPortType type.
     * 
     * @param type
     *            numeric value of the lc mapping port type
     * 
     * @return LcMappingPortType
     */
    public static LcMappingPortType valueOf(byte type) {
        LcMappingPortType result = null;

        for (LcMappingPortType e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a LcMappingPortType type");
        }
        return result;
    }

}
