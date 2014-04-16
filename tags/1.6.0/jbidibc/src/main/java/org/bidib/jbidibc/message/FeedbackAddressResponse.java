package org.bidib.jbidibc.message;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.LinkedList;

import org.bidib.jbidibc.AddressData;
import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.AddressTypeEnum;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

/**
 * Signal the detection of a loco in the specified section.
 */
public class FeedbackAddressResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_BM_ADDRESS;

    FeedbackAddressResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length < 3) {
            throw new ProtocolException("no feedback address received");
        }
    }

    public FeedbackAddressResponse(byte[] addr, int num, int detectorNumber, Collection<AddressData> addresses)
        throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_BM_ADDRESS, prepareAddressData(detectorNumber, addresses));
    }

    public int getDetectorNumber() {
        return ByteUtils.getInt(getData()[0], 0x7F);
    }

    private static byte[] prepareAddressData(int detectorNumber, Collection<AddressData> addresses) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(ByteUtils.getLowByte(detectorNumber));

        if (addresses.size() > 0) {
            for (AddressData addressData : addresses) {
                addressData.writeToStream(out);
                // int address = addressData.getAddress();
                // if (address > 0) {
                // // write address
                // out.write(ByteUtils.getLowByte(address));
                // byte highAddr = ByteUtils.getHighByte(address);
                // switch (addressData.getType()) {
                // case LOCOMOTIVE_BACKWARD:
                // highAddr = (byte) (highAddr & 0x3F);
                // highAddr = (byte) (highAddr | (1 << 7));
                // break;
                // case ACCESSORY:
                // highAddr = (byte) (highAddr & 0x3F);
                // highAddr = (byte) (highAddr | (1 << 6));
                // break;
                // case EXTENDED_ACCESSORY:
                // highAddr = (byte) (highAddr | (1 << 6));
                // highAddr = (byte) (highAddr | (1 << 7));
                // break;
                // default:
                // highAddr = (byte) (highAddr & 0x3F);
                // break;
                // }
                // out.write(highAddr);
                // }
                // else {
                // out.write((byte) 0);
                // out.write((byte) 0);
                // }
            }
        }
        else {
            out.write((byte) 0);
            out.write((byte) 0);
        }

        return out.toByteArray();
    }

    public Collection<AddressData> getAddresses() {
        Collection<AddressData> result = new LinkedList<AddressData>();
        byte[] data = getData();
        int index = 1;

        while (index < data.length) {
            byte lowByte = data[index++];
            byte highByte = data[index++];
            int address = ByteUtils.getWord(lowByte, (byte) (highByte & 0x3F));

            if (address > 0) {
                result.add(new AddressData(address, AddressTypeEnum.valueOf((byte) ((highByte & 0xC0) >> 6))));
            }
        }
        return result;
    }
}
