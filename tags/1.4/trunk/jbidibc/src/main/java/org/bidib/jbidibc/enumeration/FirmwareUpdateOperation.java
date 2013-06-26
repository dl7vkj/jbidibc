package org.bidib.jbidibc.enumeration;

import org.bidib.jbidibc.BidibLibrary;

public enum FirmwareUpdateOperation implements BidibEnum {
    //@formatter:off
    ENTER(BidibLibrary.BIDIB_MSG_FW_UPDATE_OP_ENTER), EXIT(BidibLibrary.BIDIB_MSG_FW_UPDATE_OP_EXIT), SETDEST(
        BidibLibrary.BIDIB_MSG_FW_UPDATE_OP_SETDEST), DATA(BidibLibrary.BIDIB_MSG_FW_UPDATE_OP_DATA), DONE(
        BidibLibrary.BIDIB_MSG_FW_UPDATE_OP_DONE);
    //@formatter:on

    private final byte type;

    FirmwareUpdateOperation(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    /**
     * Create a firmware update operation.
     * 
     * @param type
     *            numeric value of the firmware update operation
     * 
     * @return FirmwareUpdateOperation
     */
    public static FirmwareUpdateOperation valueOf(byte type) {
        FirmwareUpdateOperation result = null;

        for (FirmwareUpdateOperation e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a firmware update operation");
        }
        return result;
    }
}
