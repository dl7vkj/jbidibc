package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessoryParaResponse extends BidibMessage {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccessoryParaResponse.class);

    public static final Integer TYPE = BidibLibrary.MSG_ACCESSORY_PARA;

    public static final byte UNKNOWN_PARA = ByteUtils.getLowByte(255);

    AccessoryParaResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length < 2) {
            throw new ProtocolException("no accessory parameter received");
        }
    }

    public AccessoryParaResponse(byte[] addr, int num, byte accessoryNum, byte paraNum, byte[] value)
        throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_ACCESSORY_PARA, ByteUtils.concat(new byte[] { accessoryNum, paraNum }, value));
    }

    public String getName() {
        return "MSG_ACCESSORY_PARA";
    }

    public int getAccessoryNumber() {
        return getData()[0];
    }

    public int getParameter() {
        return ByteUtils.getInt(getData()[1]);
    }

    public byte[] getValue() {
        byte[] data = getData();

        if (data.length > 2) {
            byte[] result = new byte[data.length - 2];

            System.arraycopy(data, 2, result, 0, result.length);
            return result;
        }
        LOGGER.warn("No values received! Current accessory: {}, param: {}", getAccessoryNumber(), getParameter());
        return null;
    }
}
