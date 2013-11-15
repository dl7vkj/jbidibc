package org.bidib.jbidibc.message;

import java.util.Collection;
import java.util.LinkedList;

import org.bidib.jbidibc.AddressData;
import org.bidib.jbidibc.enumeration.AddressTypeEnum;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

/**
 * Signal the the detection of a loco in the specified section. 
 */
public class FeedbackAddressResponse extends BidibMessage {
    FeedbackAddressResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length < 3) {
            throw new ProtocolException("no feedback address received");
        }
    }

    public int getDetectorNumber() {
        return ByteUtils.getInt(getData()[0]);
    }

    public Collection<AddressData> getAddresses() {
        Collection<AddressData> result = new LinkedList<AddressData>();
        byte[] data = getData();
        int index = 1;

        while (index < data.length) {
            byte lowByte = data[index++];
            byte highByte = data[index++];
            int address = ByteUtils.getWord(lowByte, highByte);

            if (address > 0) {
                result.add(new AddressData(address, AddressTypeEnum.valueOf((byte) ((highByte & 0xC0) >> 6))));
            }
        }
        return result;
    }
}
