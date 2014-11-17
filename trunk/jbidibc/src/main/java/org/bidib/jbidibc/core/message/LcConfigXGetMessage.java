package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.enumeration.LcOutputType;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;

public class LcConfigXGetMessage extends BidibCommandMessage {

    protected LcConfigXGetMessage(LcOutputType outputType, int outputNumber) {
        super(0, BidibLibrary.MSG_LC_CONFIGX_GET, new byte[] { outputType.getType(), (byte) outputNumber });
    }

    public LcConfigXGetMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_LC_CONFIGX_GET";
    }

    public LcOutputType getPortType() {
        return LcOutputType.valueOf(getData()[0]);
    }

    public int getPortNumber() {
        return ByteUtils.getInt(getData()[1]);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null /* new Integer[] { LcConfigXResponse.TYPE } */;
    }
}
