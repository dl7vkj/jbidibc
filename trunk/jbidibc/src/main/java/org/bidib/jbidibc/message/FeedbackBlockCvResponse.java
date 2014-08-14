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
 * Signal the block CV message from feedback device.
 */
public class FeedbackBlockCvResponse extends BidibMessage {
    private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackBlockCvResponse.class);

    public static final Integer TYPE = BidibLibrary.MSG_BM_BLOCK_CV;

    FeedbackBlockCvResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 12) {
            throw new ProtocolException("no feedback block CV received");
        }
    }

    public FeedbackBlockCvResponse(byte[] addr, int num, int vendorId, int serialNum, int offset, int indexRegister,
        byte[] data) throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_BM_BLOCK_CV, prepareData(vendorId, serialNum, offset, indexRegister, data));
    }

    public String getName() {
        return "MSG_BM_BLOCK_CV";
    }

    private static byte[] prepareData(int vendorId, int serialNum, int offset, int indexRegister, byte[] data) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(ByteUtils.getLowByte(vendorId));

        byte[] serial = ByteUtils.toDWORD(serialNum);
        out.write(serial, 0, serial.length);

        out.write(ByteUtils.getLowByte(offset));

        out.write(ByteUtils.getLowByte(indexRegister));
        out.write(ByteUtils.getHighByte(indexRegister));

        out.write(data, 0, data.length);
        return out.toByteArray();
    }

    public int getDecoderVendorId() {
        return ByteUtils.getInt(getData()[0]);
    }

    public int getDecoderSerialNum() {
        return ByteUtils.convertSerial(getData(), 1);
    }

    public int getOffset() {
        return ByteUtils.getInt(getData()[5]);
    }

    public int getIndexRegister() {
        return ByteUtils.getInt(getData()[6], getData()[7]);
    }

    public byte[] getDat() {
        return new byte[] { getData()[8], getData()[9], getData()[10], getData()[11] };
    }

    public AddressData getAddress() {
        byte[] data = getData();
        int index = 0;

        byte decVid = data[index++];
        if (decVid > 0) {
            // the decVid is > 0 -> no address
            return null;
        }

        byte lowByte = data[index++];
        byte highByte = data[index++];
        int address = ByteUtils.getWord(lowByte, (byte) (highByte & 0x3F));
        LOGGER.debug("Return the address of MSG_BM_BLOCK_CV: {}", address);
        return new AddressData(address, AddressTypeEnum.valueOf((byte) ((highByte & 0xC0) >> 6)));
    }

    public int getCvNumber() {
        // Adresse = IDXH * 65536 + IDXL * 256 + OFFSET **)
        byte[] data = getData();
        int index = 5;

        int offset = ByteUtils.getInt(data[index++]);
        int idxl = ByteUtils.getInt(data[index++]);
        int idxh = ByteUtils.getInt(data[index++]);

        int cvNumber = idxh * 65536 + idxl * 256 + offset;

        cvNumber++; // 0 -> CV 1

        LOGGER.debug("Return the cvNumber of MSG_BM_BLOCK_CV: {}", cvNumber);
        return cvNumber;
    }

}
