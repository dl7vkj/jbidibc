package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.enumeration.ActivateCoilEnum;
import org.bidib.jbidibc.core.enumeration.AddressTypeEnum;
import org.bidib.jbidibc.core.enumeration.TimeBaseUnitEnum;
import org.bidib.jbidibc.core.enumeration.TimingControlEnum;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.message.BidibMessage;
import org.bidib.jbidibc.core.message.CommandStationAccessoryMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CommandStationAccessoryMessageTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandStationAccessoryMessageTest.class);

    @Test
    public void createCommandStationAccessoryMessageFromByteArray() throws ProtocolException {
        byte[] message = { 0x08, 0x02, 0x00, (byte) 0xD6, 0x65, 0x16, 0x00, 0x00, (byte) 0x81 };

        BidibMessage bidibMessage = new BidibMessage(message);

        Assert.assertEquals(bidibMessage.getType(), BidibLibrary.MSG_CS_ACCESSORY);
    }

    @Test
    public void createCommandStationAccessoryMessage() throws ProtocolException {
        int address = 19;
        AddressTypeEnum addressType = AddressTypeEnum.ACCESSORY;
        TimingControlEnum timingControl = TimingControlEnum.OUTPUT_UNIT;
        ActivateCoilEnum activateCoil = ActivateCoilEnum.COIL_OFF;
        int aspect = 1;
        TimeBaseUnitEnum timeBaseUnit = TimeBaseUnitEnum.UNIT_1S;
        byte time = 0x31;

        CommandStationAccessoryMessage message =
            new CommandStationAccessoryMessage(address, addressType, timingControl, activateCoil, aspect, timeBaseUnit,
                time);
        LOGGER.info("Prepare message: {}", message);

        Assert.assertEquals(message.getType(), BidibLibrary.MSG_CS_ACCESSORY);
        Assert.assertNotNull(message.getDecoderAddress());
        Assert.assertEquals(message.getDecoderAddress().getAddress(), 19);
        Assert.assertEquals(message.getDecoderAddress().getType(), AddressTypeEnum.ACCESSORY);
        Assert.assertEquals(message.getTimingControl(), TimingControlEnum.OUTPUT_UNIT);
        Assert.assertEquals(message.getActivateCoil(), ActivateCoilEnum.COIL_OFF);
        Assert.assertEquals(message.getAspect(), 1);
        Assert.assertEquals(message.getTimeBaseUnit(), TimeBaseUnitEnum.UNIT_1S);
        Assert.assertEquals(message.getTime(), 0x31);
    }

    @Test
    public void createCommandStationAccessoryMessage2() throws ProtocolException {
        int address = 19;
        AddressTypeEnum addressType = AddressTypeEnum.ACCESSORY;
        TimingControlEnum timingControl = TimingControlEnum.COIL_ON_OFF;
        ActivateCoilEnum activateCoil = ActivateCoilEnum.COIL_ON;
        int aspect = 0x1F;
        TimeBaseUnitEnum timeBaseUnit = TimeBaseUnitEnum.UNIT_1S;
        byte time = 0x30;

        CommandStationAccessoryMessage message =
            new CommandStationAccessoryMessage(address, addressType, timingControl, activateCoil, aspect, timeBaseUnit,
                time);
        LOGGER.info("Prepare message: {}", message);

        Assert.assertEquals(message.getType(), BidibLibrary.MSG_CS_ACCESSORY);
        Assert.assertNotNull(message.getDecoderAddress());
        Assert.assertEquals(message.getDecoderAddress().getAddress(), 19);
        Assert.assertEquals(message.getDecoderAddress().getType(), AddressTypeEnum.ACCESSORY);
        Assert.assertEquals(message.getTimingControl(), TimingControlEnum.COIL_ON_OFF);
        Assert.assertEquals(message.getActivateCoil(), ActivateCoilEnum.COIL_ON);
        Assert.assertEquals(message.getAspect(), 0x1F);
        Assert.assertEquals(message.getTimeBaseUnit(), TimeBaseUnitEnum.UNIT_1S);
        Assert.assertEquals(message.getTime(), 0x30);
    }

    @Test
    public void createCommandStationExtendedAccessoryMessage() throws ProtocolException {
        int address = 19;
        AddressTypeEnum addressType = AddressTypeEnum.EXTENDED_ACCESSORY;
        TimingControlEnum timingControl = TimingControlEnum.OUTPUT_UNIT;
        ActivateCoilEnum activateCoil = ActivateCoilEnum.COIL_OFF;
        int aspect = 0;
        TimeBaseUnitEnum timeBaseUnit = TimeBaseUnitEnum.UNIT_100MS;
        // byte data = (byte) 0x80;
        byte time = 0x30;

        CommandStationAccessoryMessage message =
            new CommandStationAccessoryMessage(address, addressType, timingControl, activateCoil, aspect, timeBaseUnit,
                time);
        LOGGER.info("Prepare message: {}", message);

        Assert.assertEquals(message.getType(), BidibLibrary.MSG_CS_ACCESSORY);
        Assert.assertNotNull(message.getDecoderAddress());
        Assert.assertEquals(message.getDecoderAddress().getAddress(), 19);
        Assert.assertEquals(message.getDecoderAddress().getType(), AddressTypeEnum.EXTENDED_ACCESSORY);
        Assert.assertEquals(message.getTimingControl(), TimingControlEnum.OUTPUT_UNIT);
        Assert.assertEquals(message.getActivateCoil(), ActivateCoilEnum.COIL_OFF);
        Assert.assertEquals(message.getAspect(), 0);
        Assert.assertEquals(message.getTimeBaseUnit(), TimeBaseUnitEnum.UNIT_100MS);
        Assert.assertEquals(message.getTime(), 0x30);
    }

}
