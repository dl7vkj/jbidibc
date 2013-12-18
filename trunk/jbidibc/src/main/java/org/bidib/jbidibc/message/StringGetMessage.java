package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.utils.ByteUtils;

public class StringGetMessage extends BidibMessage {
    public StringGetMessage(int namespace, int stringId) {
        super(0, BidibLibrary.MSG_STRING_GET, new byte[] { ByteUtils.getLowByte(namespace),
            ByteUtils.getLowByte(stringId) });
    }
}
