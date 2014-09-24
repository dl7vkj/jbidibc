package org.bidib.jbidibc.message;

import java.io.ByteArrayOutputStream;

import org.bidib.jbidibc.AddressData;
import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.AddressTypeEnum;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Signal the detection of a dynamic state from loco in the specified section.
 */
public class FeedbackDynStateResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_BM_DYN_STATE;

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackDynStateResponse.class);

    FeedbackDynStateResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 5) {
            throw new ProtocolException("no feedback dynamic state received");
        }
    }

    public FeedbackDynStateResponse(byte[] addr, int num, int detectorNumber, byte dynNumber, byte dynValue)
        throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_BM_DYN_STATE, prepareDynamicStateData(detectorNumber, dynNumber, dynValue));
    }

    public String getName() {
        return "MSG_BM_DYN_STATE";
    }

    public int getDetectorNumber() {
        return ByteUtils.getInt(getData()[0], 0x7F);
    }

    private static byte[] prepareDynamicStateData(int detectorNumber, byte dynNumber, byte dynValue) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(ByteUtils.getLowByte(detectorNumber));

        out.write(dynNumber);
        out.write(dynValue);

        return out.toByteArray();
    }

    public AddressData getAddress() {
        byte[] data = getData();
        int index = 1;

        byte lowByte = data[index++];
        byte highByte = data[index];
        int address = ByteUtils.getWord(lowByte, (byte) (highByte & 0x3F));
        LOGGER.debug("Return the address of MSG_BM_DYN_STATE: {}", address);
        return new AddressData(address, AddressTypeEnum.valueOf((byte) ((highByte & 0xC0) >> 6)));
    }

    public int getDynNumber() {
        return ByteUtils.getInt(getData()[1], 0xFF);
    }

    public int getDynValue() {
        return ByteUtils.getInt(getData()[2], 0xFF);
    }
}
