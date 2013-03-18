package org.bidib.jbidibc.enumeration;

import org.bidib.jbidibc.BidibLibrary;

public enum LcOutputType implements BidibEnum {
    //@formatter:off
    SWITCHPORT(BidibLibrary.BIDIB_OUTTYPE_SPORT), LIGHTPORT(BidibLibrary.BIDIB_OUTTYPE_LPORT), SERVOPORT(
        BidibLibrary.BIDIB_OUTTYPE_SERVO), SOUNDPORT(BidibLibrary.BIDIB_OUTTYPE_SOUND), MOTORPORT(
        BidibLibrary.BIDIB_OUTTYPE_MOTOR), ANALOGPORT(BidibLibrary.BIDIB_OUTTYPE_ANALOG), DELAY(
        BidibLibrary.BIDIB_MSYS_DELAY_FIXED), RANDOM_DELAY(BidibLibrary.BIDIB_MSYS_DELAY_RANDOM), INPUT_QUERY0(
        BidibLibrary.BIDIB_MSYS_INPUT_QUERY0), INPUT_QUERY1(BidibLibrary.BIDIB_MSYS_INPUT_QUERY1), FLAG_CLEAR(
        BidibLibrary.BIDIB_MSYS_FLAG_CLEAR), FLAG_SET(BidibLibrary.BIDIB_MSYS_FLAG_SET), FLAG_QUERY(
        BidibLibrary.BIDIB_MSYS_FLAG_QUERY), END_CRITICAL(BidibLibrary.BIDIB_MSYS_END_CRITCAL), BEGIN_CRITICAL(
        BidibLibrary.BIDIB_MSYS_BEGIN_CRITCAL), STOP_MACRO(BidibLibrary.BIDIB_MSYS_STOP_MACRO), START_MACRO(
        BidibLibrary.BIDIB_MSYS_START_MACRO), END_OF_MACRO(BidibLibrary.BIDIB_MSYS_END_OF_MACRO);
    //@formatter:on

    private final byte type;

    LcOutputType(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    /**
     * Create an output type.
     * 
     * @param type
     *            numeric value of the output type
     * 
     * @return OutputType
     */
    public static LcOutputType valueOf(byte type) {
        LcOutputType result = null;

        for (LcOutputType e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to an output type");
        }
        return result;
    }
}
