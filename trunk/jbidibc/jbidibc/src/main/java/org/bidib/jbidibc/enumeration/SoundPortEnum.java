package org.bidib.jbidibc.enumeration;

public enum SoundPortEnum implements BidibEnum {
    START(0);

    private final byte type;

    SoundPortEnum(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    /**
     * Create a sound port enum.
     * 
     * @param type
     *            numeric value of the sound port enum
     * 
     * @return SoundPortEnum
     */
    public static SoundPortEnum valueOf(byte type) {
        SoundPortEnum result = null;

        for (SoundPortEnum e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a sound port enum");
        }
        return result;
    }
}
