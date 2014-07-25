package org.bidib.jbidibc.message;

import java.io.ByteArrayOutputStream;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Signal the feedback of an accessory decoder.
 */
public class FeedbackAccessoryResponse extends BidibMessage {
    private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackAccessoryResponse.class);

    public static final Integer TYPE = BidibLibrary.MSG_BM_ACCESSORY;

    FeedbackAccessoryResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 3) {
            throw new ProtocolException("no feedback accessory received");
        }
    }

    public FeedbackAccessoryResponse(byte[] addr, int num, int detectorNumber, int address) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_BM_ACCESSORY, prepareAddressData(detectorNumber, address));
    }

    public String getName() {
        return "MSG_BM_ACCESSORY";
    }

    public int getDetectorNumber() {
        return ByteUtils.getInt(getData()[0], 0x7F);
    }

    private static byte[] prepareAddressData(int detectorNumber, int address) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(ByteUtils.getLowByte(detectorNumber));
        out.write(ByteUtils.getLowByte(address));
        out.write(ByteUtils.getHighByte(address) & 0x3F);

        return out.toByteArray();
    }

    public int getAddress() {
        byte[] data = getData();
        int index = 1;

        byte lowByte = data[index++];
        byte highByte = data[index++];
        int address = ByteUtils.getWord(lowByte, (byte) (highByte & 0x3F));
        LOGGER.debug("Return the address of MSG_BM_ACCESSORY: {}", address);
        return address;
    }
}
