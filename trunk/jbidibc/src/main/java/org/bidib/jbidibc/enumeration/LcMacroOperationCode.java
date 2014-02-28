package org.bidib.jbidibc.enumeration;

import org.bidib.jbidibc.BidibLibrary;

public enum LcMacroOperationCode implements BidibEnum {
    // @formatter:off
    OFF(BidibLibrary.BIDIB_MACRO_OFF), START(BidibLibrary.BIDIB_MACRO_START), RESTORE(BidibLibrary.BIDIB_MACRO_RESTORE), SAVE(
        BidibLibrary.BIDIB_MACRO_SAVE), DELETE(BidibLibrary.BIDIB_MACRO_DELETE);
    // @formatter:on

    private final byte type;

    LcMacroOperationCode(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    /**
     * Create a macro operation code.
     * 
     * @param type
     *            numeric value of the macro operation code
     * 
     * @return LcMacroOperationCode
     */
    public static LcMacroOperationCode valueOf(byte type) {
        LcMacroOperationCode result = null;

        for (LcMacroOperationCode e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a macro operation code");
        }
        return result;
    }
}
