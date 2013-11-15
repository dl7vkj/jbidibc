package org.bidib.jbidibc.enumeration;

public enum FeedbackPortEnum implements BidibEnum {
    FREE(0), OCCUPIED(1);

    private final byte type;

    FeedbackPortEnum(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    /**
     * Create an feedback port enum.
     * 
     * @param type
     *            numeric value of the feedback port enum
     * 
     * @return FeedbackPortEnum
     */
    public static FeedbackPortEnum valueOf(byte type) {
        FeedbackPortEnum result = null;

        for (FeedbackPortEnum e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a feedback port enum");
        }
        return result;
    }
}
