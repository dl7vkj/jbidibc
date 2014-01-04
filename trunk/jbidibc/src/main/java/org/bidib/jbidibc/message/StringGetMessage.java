package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

public class StringGetMessage extends BidibMessage {
    public StringGetMessage(int namespace, int stringId) {
        super(0, BidibLibrary.MSG_STRING_GET, new byte[] { ByteUtils.getLowByte(namespace),
            ByteUtils.getLowByte(stringId) });
    }

    public StringGetMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public int getNamespace() {
        return ByteUtils.getInt(getData()[0]);
    }

    public int getStringId() {
        return ByteUtils.getInt(getData()[1]);
    }
}
