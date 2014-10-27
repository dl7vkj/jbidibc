package org.bidib.jbidibc.core.message;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.LcConfigX;
import org.bidib.jbidibc.core.enumeration.LcOutputType;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;

public class LcConfigXSetMessage extends BidibCommandMessage {

    public LcConfigXSetMessage(LcConfigX configX) {
        super(0, BidibLibrary.MSG_LC_CONFIGX_SET, configX.getCodedPortConfig());
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

        byte outputType = data[0];
        int portNumber = ByteUtils.getInt(data[1], 0x7F);

        Map<Byte, Number> values = new LinkedHashMap<>();
        // TODO get the values

        return new LcConfigX(LcOutputType.valueOf(outputType), portNumber, values);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { LcConfigXResponse.TYPE };
    }
}
