package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.LcConfigX;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.bidib.jbidibc.core.utils.MessageUtils;

public class LcConfigXResponse extends BidibMessage {

    public static final Integer TYPE = BidibLibrary.MSG_LC_CONFIGX;

    LcConfigXResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length < 2) {
            throw new ProtocolException("No LcConfigX response received.");
        }
    }

    public LcConfigXResponse(byte[] addr, int num, byte portType, byte portNum, byte[] config) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_LC_CONFIGX, ByteUtils.concat(new byte[] { portType, portNum }, config));
    }

    public String getName() {
        return "MSG_LC_CONFIGX";
    }

    public LcConfigX getLcConfigX() {
        byte[] data = getData();
        return MessageUtils.getLcConfigX(data);
    }
}
