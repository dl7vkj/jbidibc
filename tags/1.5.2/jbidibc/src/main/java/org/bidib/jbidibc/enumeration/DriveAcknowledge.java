package org.bidib.jbidibc.enumeration;

public enum DriveAcknowledge implements BidibEnum {
    //@formatter:off
    NOT_ACKNOWLEDGED(0), ACKNOWLEDGED(1), DELAYED(2);
    //@formatter:on

    private final byte type;

    DriveAcknowledge(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    /**
     * Create a drive acknowledge.
     * 
     * @param type
     *            numeric value of the drive acknowledge
     * 
     * @return DriveAcknowledge
     */
    public static DriveAcknowledge valueOf(byte type) {
        DriveAcknowledge result = null;

        for (DriveAcknowledge e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a drive acknowledge");
        }
        return result;
    }
}
