package org.bidib.jbidibc.core.enumeration;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.utils.ByteUtils;

public enum LcOutputType implements BidibEnum, PortStatusAware {
    // @formatter:off
    SWITCHPORT(BidibLibrary.BIDIB_OUTTYPE_SPORT, true), LIGHTPORT(BidibLibrary.BIDIB_OUTTYPE_LPORT, true), SERVOPORT(
        BidibLibrary.BIDIB_OUTTYPE_SERVO, true), SOUNDPORT(BidibLibrary.BIDIB_OUTTYPE_SOUND, true), MOTORPORT(
        BidibLibrary.BIDIB_OUTTYPE_MOTOR, true), ANALOGPORT(BidibLibrary.BIDIB_OUTTYPE_ANALOG, true), BACKLIGHTPORT(
        BidibLibrary.BIDIB_OUTTYPE_BACKLIGHT, true), INPUTPORT(BidibLibrary.BIDIB_OUTTYPE_INPUT, true), DELAY(
        BidibLibrary.BIDIB_MSYS_DELAY_FIXED), RANDOM_DELAY(BidibLibrary.BIDIB_MSYS_DELAY_RANDOM), INPUT_QUERY0(
        BidibLibrary.BIDIB_MSYS_INPUT_QUERY0), INPUT_QUERY1(BidibLibrary.BIDIB_MSYS_INPUT_QUERY1), FLAG_CLEAR(
        BidibLibrary.BIDIB_MSYS_FLAG_CLEAR), FLAG_SET(BidibLibrary.BIDIB_MSYS_FLAG_SET), FLAG_QUERY(
        BidibLibrary.BIDIB_MSYS_FLAG_QUERY1), FLAG_QUERY1(BidibLibrary.BIDIB_MSYS_FLAG_QUERY1), FLAG_QUERY0(
        BidibLibrary.BIDIB_MSYS_FLAG_QUERY0), END_CRITICAL(BidibLibrary.BIDIB_MSYS_END_CRITCAL), BEGIN_CRITICAL(
        BidibLibrary.BIDIB_MSYS_BEGIN_CRITCAL), STOP_MACRO(BidibLibrary.BIDIB_MSYS_STOP_MACRO), START_MACRO(
        BidibLibrary.BIDIB_MSYS_START_MACRO), END_OF_MACRO(BidibLibrary.BIDIB_MSYS_END_OF_MACRO), ACCESSORY_OKAY_INPUTQUERY0(
        BidibLibrary.BIDIB_MSYS_ACC_OKAY_QIN0), ACCESSORY_OKAY_INPUTQUERY1(BidibLibrary.BIDIB_MSYS_ACC_OKAY_QIN1), ACCESSORY_OKAY_NF(
        BidibLibrary.BIDIB_MSYS_ACC_OKAY_NF), SERVOMOVE_QUERY(BidibLibrary.BIDIB_MSYS_SERVOMOVE_QUERY);
    // @formatter:on

    private final byte type;

    private final boolean hasPortStatus;

    LcOutputType(int type) {
        this(type, false);
    }

    LcOutputType(int type, boolean hasPortStatus) {
        // this.type = ByteUtils.getLowByte(type);
        this.type = (byte) (type & 0xFF);
        this.hasPortStatus = hasPortStatus;
    }

    public byte getType() {
        return type;
    }

    @Override
    public boolean hasPortStatus() {
        return hasPortStatus;
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
            throw new IllegalArgumentException("cannot map " + (type & 0xFF) + " (0x" + ByteUtils.byteToHex(type)
                + ") to an output type");
        }
        return result;
    }
}
