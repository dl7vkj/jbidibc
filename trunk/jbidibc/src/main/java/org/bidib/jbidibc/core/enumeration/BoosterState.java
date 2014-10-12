package org.bidib.jbidibc.core.enumeration;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.utils.ByteUtils;

public enum BoosterState implements BidibEnum {
    // @formatter:off
    OFF(BidibLibrary.BIDIB_BST_STATE_OFF), OFF_SHORT(BidibLibrary.BIDIB_BST_STATE_OFF_SHORT), OFF_HOT(
        BidibLibrary.BIDIB_BST_STATE_OFF_HOT), OFF_NO_POWER(BidibLibrary.BIDIB_BST_STATE_OFF_NOPOWER), OFF_GO_REQ(
        BidibLibrary.BIDIB_BST_STATE_OFF_GO_REQ), OFF_HERE(BidibLibrary.BIDIB_BST_STATE_OFF_HERE), OFF_NO_DCC(
        BidibLibrary.BIDIB_BST_STATE_OFF_NO_DCC), ON(BidibLibrary.BIDIB_BST_STATE_ON), ON_LIMIT(
        BidibLibrary.BIDIB_BST_STATE_ON_LIMIT), ON_HOT(BidibLibrary.BIDIB_BST_STATE_ON_HOT), ON_STOP_REQ(
        BidibLibrary.BIDIB_BST_STATE_ON_STOP_REQ), ON_HERE(BidibLibrary.BIDIB_BST_STATE_ON_HERE);
    // @formatter:on

    private final byte type;

    BoosterState(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    /**
     * Create a booster status.
     * 
     * @param type
     *            numeric value of the booster status
     * 
     * @return BoosterStatus
     */
    public static BoosterState valueOf(byte type) {
        BoosterState result = null;

        for (BoosterState e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a booster status");
        }
        return result;
    }

    public static boolean isOnState(BoosterState boosterState) {
        return ((ByteUtils.getInt(boosterState.getType()) & 0x80) == 0x80);
    }

    public static boolean isOffState(BoosterState boosterState) {
        return ((ByteUtils.getInt(boosterState.getType()) & 0x80) == 0x00);
    }
}
