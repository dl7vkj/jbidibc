package org.bidib.jbidibc.core.message;

import java.io.ByteArrayOutputStream;

import org.bidib.jbidibc.core.AddressData;
import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.enumeration.AddressTypeEnum;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Signal the CV message from feedback device.
 */
public class FeedbackCvResponse extends BidibMessage {
    private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackCvResponse.class);

    public static final Integer TYPE = BidibLibrary.MSG_BM_CV;

    FeedbackCvResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 5) {
            throw new ProtocolException("no feedback CV received");
        }
    }

    public FeedbackCvResponse(byte[] addr, int num, int address, int cvNumber, byte dat) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_BM_CV, prepareData(address, cvNumber & 0x3FF, dat));
    }

    public String getName() {
        return "MSG_BM_CV";
    }

    public int getDetectorNumber() {
        return ByteUtils.getInt(getData()[0], 0x7F);
    }

    private static byte[] prepareData(int address, int cvNumber, byte data) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(ByteUtils.getLowByte(address));
        out.write(ByteUtils.getHighByte(address) & 0x3F);

        out.write(ByteUtils.getLowByte(cvNumber));
        out.write(ByteUtils.getHighByte(cvNumber));

        out.write(data);
        return out.toByteArray();
    }

    public AddressData getAddress() {
        byte[] data = getData();
        int index = 0;

        byte lowByte = data[index++];
        byte highByte = data[index++];
        int address = ByteUtils.getWord(lowByte, (byte) (highByte & 0x3F));
        LOGGER.debug("Return the address of MSG_BM_CV: {}", address);
        return new AddressData(address, AddressTypeEnum.valueOf((byte) ((highByte & 0xC0) >> 6)));
    }

    public int getCvNumber() {
        byte[] data = getData();
        int index = 2;

        byte lowByte = data[index++];
        byte highByte = data[index++];
        int cvNumber = ByteUtils.getWord(lowByte, (byte) (highByte & 0x3F));

        cvNumber++; // 0 -> CV 1

        LOGGER.debug("Return the cvNumber of MSG_BM_CV: {}", cvNumber);
        return cvNumber;
    }

    public int getDat() {
        return ByteUtils.getInt(getData()[4]);
    }
}
