package org.bidib.jbidibc.enumeration;

import org.bidib.jbidibc.BidibLibrary;

public enum BoosterState implements BidibEnum {
    //@formatter:off
    OFF(BidibLibrary.BIDIB_BST_STATE_OFF),
    SHORT(BidibLibrary.BIDIB_BST_STATE_SHORT),
    OFF_HOT(BidibLibrary.BIDIB_BST_STATE_OFF_HOT),
    NO_POWER(BidibLibrary.BIDIB_BST_STATE_NOPOWER),
    GO_REQ(BidibLibrary.BIDIB_BST_STATE_OFF_GO_REQ),
    ON(BidibLibrary.BIDIB_BST_STATE_ON),
    ON_LIMIT(BidibLibrary.BIDIB_BST_STATE_ON_LIMIT),
    ON_HOT(BidibLibrary.BIDIB_BST_STATE_ON_HOT),
    ON_STOP_REQ(BidibLibrary.BIDIB_BST_STATE_ON_STOP_REQ);
    // TODO add missing state BIDIB_BST_STATE_ON_HERE
    // ON_HERE(BidibLibrary.BIDIB_BST_STATE_ON_HERE);
    //@formatter:on

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
}
