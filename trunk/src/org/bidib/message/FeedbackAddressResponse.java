package org.bidib.message;

import java.util.Collection;
import java.util.LinkedList;

import org.bidib.AddressData;
import org.bidib.enumeration.AddressTypeEnum;
import org.bidib.exception.ProtocolException;

public class FeedbackAddressResponse extends BidibMessage {
    FeedbackAddressResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length < 3) {
            throw new ProtocolException("no feedback address received");
        }
    }

    public int getDetectorNumber() {
        return getData()[0];
    }

    public Collection<AddressData> getAddresses() {
        Collection<AddressData> result = new LinkedList<AddressData>();
        byte[] data = getData();
        int index = 1;

        while (index < data.length) {
            byte lowByte = data[index++];
            byte highByte = data[index++];

            result.add(new AddressData(((highByte & 0x3F) << 8) + (lowByte & 0xFF), AddressTypeEnum
                    .valueOf((byte) (highByte >> 6))));
        }
        return result;
    }
}
