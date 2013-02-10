package org.bidib.jbidibc.enumeration;

public enum SpeedStepsEnum implements BidibEnum {
    //@formatter:off
    DCC14(0x00), DCC28(0x02), DCC128(0x03);
    //@formatter:on
    private final byte type;

    SpeedStepsEnum(int type) {
        this.type = (byte) type;
    }

    @Override
    public byte getType() {
        return type;
    }

    /**
     * Create a speed steps enum value.
     * 
     * @param type
     *            numeric value of the speed steps enum value
     * 
     * @return SpeedSteps
     */
    public static SpeedStepsEnum valueOf(byte type) {
        SpeedStepsEnum result = null;

        for (SpeedStepsEnum e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a speed steps enum value");
        }
        return result;
    }
}
