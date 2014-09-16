package org.bidib.jbidibc.message;

import org.bidib.jbidibc.AddressData;
import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.enumeration.ActivateCoilEnum;
import org.bidib.jbidibc.enumeration.AddressTypeEnum;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Response from command station with the information of manual accessory change
 */
public class CommandStationAccessoryManualResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_CS_ACCESSORY_MANUAL;

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandStationAccessoryManualResponse.class);

    private static final int DATA_INDEX = 2;

    CommandStationAccessoryManualResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 3) {
            throw new ProtocolException("no command station accessory manual received");
        }

        LOGGER.debug("Received manual accessory, aspect: {}", getAspect());
    }

    public String getName() {
        return "MSG_CS_ACCESSORY_MANUAL";
    }

    // public int getAddress() {
    // byte[] data = getData();
    //
    // return ByteUtils.getInt(data[0], data[1]);
    // }
    public AddressData getDecoderAddress() {
        LOGGER.debug("Get accessory decoder address from data: {}", ByteUtils.bytesToHex(getData()));

        int index = 0;
        byte lowByte = getData()[index++];
        byte highByte = getData()[index++];
        int address = ByteUtils.getWord(lowByte, highByte);

        byte data0 = getData()[DATA_INDEX];

        AddressData addressData =
            new AddressData(address,
                ((data0 & 0x80) == 0x80 ? AddressTypeEnum.EXTENDED_ACCESSORY : AddressTypeEnum.ACCESSORY));
        LOGGER.debug("Prepared address data: {}", addressData);
        return addressData;
    }

    public ActivateCoilEnum getActivate() {
        byte type = (byte) ((ByteUtils.getInt(getData()[DATA_INDEX]) & 0x20) >> 5);
        return ActivateCoilEnum.valueOf(type);
    }

    public int getAspect() {
        return ByteUtils.getInt(getData()[DATA_INDEX]) & 0x1F;
    }
}
