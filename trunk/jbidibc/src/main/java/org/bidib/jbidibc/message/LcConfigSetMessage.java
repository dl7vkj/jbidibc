package org.bidib.jbidibc.message;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.LcConfig;
import org.bidib.jbidibc.enumeration.LcOutputType;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

public class LcConfigSetMessage extends BidibCommandMessage {
    public LcConfigSetMessage(LcConfig config) {
        super(0, BidibLibrary.MSG_LC_CONFIG_SET, new byte[] { config.getOutputType().getType(),
            (byte) config.getOutputNumber(), (byte) config.getValue1(), (byte) config.getValue2(),
            (byte) config.getValue3(), (byte) config.getValue4() });
    }

    public LcConfigSetMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public LcOutputType getPortType() {
        return LcOutputType.valueOf(getData()[0]);
    }

    public int getPortNumber() {
        return ByteUtils.getInt(getData()[1]);
    }

    public byte[] getPortConfig() {
        byte[] config = new byte[] { getData()[2], getData()[3], getData()[4], getData()[5] };
        return config;
    }

    public byte getValue1() {
        return getData()[2];
    }

    public byte getValue2() {
        return getData()[3];
    }

    public byte getValue3() {
        return getData()[4];
    }

    public byte getValue4() {
        return getData()[5];
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { LcConfigResponse.TYPE, LcNotAvailableResponse.TYPE };
    }
}
