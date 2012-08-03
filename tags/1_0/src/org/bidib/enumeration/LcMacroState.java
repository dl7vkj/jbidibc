package org.bidib.enumeration;

import org.bidib.BidibLibrary;


public enum LcMacroState implements BidibEnum {
    //@formatter:off
    OFF(BidibLibrary.BIDIB_MACRO_OFF),
    START(BidibLibrary.BIDIB_MACRO_START),
    RUNNING(BidibLibrary.BIDIB_MACRO_RUNNING),
    SAVE(BidibLibrary.BIDIB_MACRO_SAVE),
    DELETE(BidibLibrary.BIDIB_MACRO_DELETE),
    NOTEXIST(BidibLibrary.BIDIB_MACRO_NOTEXIST);
    //@formatter:on

    private final byte type;

    LcMacroState(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    /**
     * Create a macro state.
     * 
     * @param type
     *            numeric value of the macro state
     * 
     * @return LcMacroState
     */
    public static LcMacroState valueOf(byte type) {
        LcMacroState result = null;

        for (LcMacroState e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a macro state");
        }
        return result;
    }
}
