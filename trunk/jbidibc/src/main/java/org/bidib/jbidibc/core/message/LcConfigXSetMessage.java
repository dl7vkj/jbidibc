package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.LcConfigX;
import org.bidib.jbidibc.core.enumeration.LcOutputType;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.bidib.jbidibc.core.utils.MessageUtils;

public class LcConfigXSetMessage extends BidibCommandMessage {

    public LcConfigXSetMessage(final LcConfigX configX) {
        super(0, BidibLibrary.MSG_LC_CONFIGX_SET, MessageUtils.getCodedPortConfig(configX));
    }

    public LcConfigXSetMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_LC_CONFIGX_SET";
    }

    public LcOutputType getPortType() {
        return LcOutputType.valueOf(getData()[0]);
    }

    public int getPortNumber() {
        return ByteUtils.getInt(getData()[1], 0x7F);
    }

    public LcConfigX getLcConfigX() {
        byte[] data = getData();
        return MessageUtils.getLcConfigX(data);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { LcConfigXResponse.TYPE };
    }
}
