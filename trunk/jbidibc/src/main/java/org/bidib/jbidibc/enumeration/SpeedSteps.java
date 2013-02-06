package org.bidib.jbidibc.enumeration;

public enum SpeedSteps implements BidibEnum {
    //@formatter:off
    DCC14(0x00/*00*/), DCC28(/*0x0010*/0x02), DCC128(/*0x0011*/0x03);
    //@formatter:on
    private final byte type;

    SpeedSteps(int type) {
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
    public static SpeedSteps valueOf(byte type) {
        SpeedSteps result = null;

        for (SpeedSteps e : values()) {
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
