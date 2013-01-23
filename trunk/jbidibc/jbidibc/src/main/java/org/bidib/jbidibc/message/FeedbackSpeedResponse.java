package org.bidib.jbidibc.message;

import org.bidib.jbidibc.AddressData;
import org.bidib.jbidibc.enumeration.AddressTypeEnum;
import org.bidib.jbidibc.exception.ProtocolException;

public class FeedbackSpeedResponse extends BidibMessage {
    FeedbackSpeedResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 4) {
            throw new ProtocolException("no feedback speed received");
        }
    }

    public AddressData getAddress() {
        byte[] data = getData();

        return new AddressData(FeedbackAddressResponse.getWord(data[0], data[1]),
                AddressTypeEnum.valueOf((byte) ((data[1] & 0xC0) >> 6)));
    }

    public int getSpeed() {
        byte[] data = getData();

        return FeedbackAddressResponse.getWord(data[2], data[3]);
    }
}
