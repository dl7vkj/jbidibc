package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.AddressData;
import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.enumeration.ActivateCoilEnum;
import org.bidib.jbidibc.core.enumeration.AddressTypeEnum;
import org.bidib.jbidibc.core.enumeration.TimeBaseUnitEnum;
import org.bidib.jbidibc.core.enumeration.TimingControlEnum;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command to control an accessory decoder
 */
public class CommandStationAccessoryMessage extends BidibCommandMessage {
    public static final Integer TYPE = BidibLibrary.MSG_CS_ACCESSORY;

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandStationAccessoryMessage.class);

    private static final int DATA_INDEX = 2;

    private static final int TIME_INDEX = 3;

    public CommandStationAccessoryMessage(int address, AddressTypeEnum addressType, TimingControlEnum timingControl,
        ActivateCoilEnum activateCoil, int aspect, TimeBaseUnitEnum timeBaseUnit, int time) {
        super(0, BidibLibrary.MSG_CS_ACCESSORY, (byte) (address & 0xFF), (byte) ((address & 0xFF00) >> 8),
            (byte) (((AddressTypeEnum.ACCESSORY.equals(addressType) ? 0 : 1) << 7)
                | ((TimingControlEnum.COIL_ON_OFF.equals(timingControl) ? 0 : 1) << 6)
                | ((ActivateCoilEnum.COIL_OFF.equals(activateCoil) ? 0 : 1) << 5) | (aspect & 0x1F)),
            (byte) (((TimeBaseUnitEnum.UNIT_100MS.equals(timeBaseUnit) ? 0 : 1) << 7) | (time & 0x7F)));
    }

    public CommandStationAccessoryMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public String getName() {
        return "MSG_CS_ACCESSORY";
    }

    public AddressData getDecoderAddress() {
        LOGGER.info("Get accessory decoder address from data: {}", ByteUtils.bytesToHex(getData()));

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

    public TimingControlEnum getTimingControl() {
        return TimingControlEnum.valueOf((byte) ((getData()[DATA_INDEX] & 0x40) >> 6));
    }

    public ActivateCoilEnum getActivateCoil() {
        return ActivateCoilEnum.valueOf((byte) ((getData()[DATA_INDEX] & 0x20) >> 5));
    }

    public int getAspect() {
        return (getData()[DATA_INDEX] & 0x1F);
    }

    public int getTime() {
        return (getData()[TIME_INDEX] & 0x7F);
    }

    public TimeBaseUnitEnum getTimeBaseUnit() {
        return TimeBaseUnitEnum.valueOf((byte) ((getData()[TIME_INDEX] & 0x80) >> 7));
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return new Integer[] { CommandStationAccessoryAcknowledgeResponse.TYPE };
    }
}
