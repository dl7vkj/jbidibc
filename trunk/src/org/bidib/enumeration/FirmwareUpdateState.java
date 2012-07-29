package org.bidib.enumeration;

import org.bidib.BidibLibrary;

public enum FirmwareUpdateState implements BidibEnum {
    //@formatter:off
    READY(BidibLibrary.BIDIB_MSG_FW_UPDATE_STAT_READY),
    EXIT(BidibLibrary.BIDIB_MSG_FW_UPDATE_STAT_EXIT),
    DATA(BidibLibrary.BIDIB_MSG_FW_UPDATE_STAT_DATA),
    ERROR(BidibLibrary.BIDIB_MSG_FW_UPDATE_STAT_ERROR);
    //@formatter:on

    private final byte type;

    FirmwareUpdateState(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    /**
     * Create a firmware update state.
     * 
     * @param type
     *            numeric value of the firmware update state
     * 
     * @return FirmwareUpdateOperation
     */
    public static FirmwareUpdateState valueOf(byte type) {
        FirmwareUpdateState result = null;

        for (FirmwareUpdateState e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a firmware update state");
        }
        return result;
    }
}
