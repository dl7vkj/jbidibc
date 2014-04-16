package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

public class LcKeyResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_LC_KEY;

    LcKeyResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 2) {
            throw new ProtocolException("no lc key received");
        }
    }

    public LcKeyResponse(byte[] addr, int num, byte keyNum, byte keyState) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_LC_KEY, new byte[] { keyNum, keyState });
    }

    public int getKeyNumber() {
        return ByteUtils.getInt(getData()[0]);
    }

    public int getKeyState() {
        return ByteUtils.getInt(getData()[1]);
    }
}
