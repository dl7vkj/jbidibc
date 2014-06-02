package org.bidib.jbidibc.message;

import java.util.BitSet;

import org.bidib.jbidibc.AddressData;
import org.bidib.jbidibc.enumeration.AddressTypeEnum;
import org.bidib.jbidibc.enumeration.DirectionEnum;
import org.bidib.jbidibc.enumeration.SpeedStepsEnum;
import org.bidib.jbidibc.exception.ProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CommandStationDriveMessageTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandStationDriveMessageTest.class);

    @Test
    public void prepareSpeedMessageBackward() {
        int address = 3;
        SpeedStepsEnum speedSteps = SpeedStepsEnum.DCC128;
        Integer speed = 51;
        DirectionEnum direction = DirectionEnum.BACKWARD;
        BitSet activeFunctions = new BitSet(8);
        activeFunctions.set(0, true);
        activeFunctions.set(1, false);
        // activeFunctions.set(1, true);
        BitSet functions = new BitSet(32);
        functions.set(4, true); // FL
        functions.set(3, false); // F4 - off
        functions.set(2, true); // F3
        functions.set(1, true); // F2
        functions.set(0, true); // F1
        CommandStationDriveMessage message =
            new CommandStationDriveMessage(address, speedSteps, speed, direction, activeFunctions, functions);

        LOGGER.debug("Created message: {}", message);
        Assert.assertNotNull(message);

        Assert.assertEquals(message.getData()[2], (byte) 0x03); // DCC128
        Assert.assertEquals(message.getData()[3], (byte) 0x03); // active

        // direction and speed
        Assert.assertEquals(message.getData()[4], (byte) 51); // speed

        // functions:
        // speed and
        // light
        // (F1...F4)
        Assert.assertEquals(message.getData()[5], (byte) 27); // FL,F3,F2,F1
    }

    @Test
    public void prepareSpeedMessageForward() {
        int address = 3;
        SpeedStepsEnum speedSteps = SpeedStepsEnum.DCC128;
        Integer speed = 51;
        DirectionEnum direction = DirectionEnum.FORWARD;
        BitSet activeFunctions = new BitSet(8);
        activeFunctions.set(0, true);
        activeFunctions.set(1, false);
        // activeFunctions.set(1, true);
        BitSet functions = new BitSet(32);
        functions.set(4, true); // FL
        functions.set(3, false); // F4 - off
        functions.set(2, true); // F3
        functions.set(1, true); // F2
        functions.set(0, true); // F1
        CommandStationDriveMessage message =
            new CommandStationDriveMessage(address, speedSteps, speed, direction, activeFunctions, functions);

        LOGGER.debug("Created message: {}", message);
        Assert.assertNotNull(message);

        Assert.assertEquals(message.getData()[2], (byte) 0x03); // DCC128
        Assert.assertEquals(message.getData()[3], (byte) 0x03); // active

        // direction and speed
        Assert.assertEquals(message.getData()[4], (byte) (51 | 0x80)); // speed

        // functions:
        // speed and
        // light
        // (F1...F4)
        Assert.assertEquals(message.getData()[5], (byte) 27); // FL,F3,F2,F1
    }

    @Test
    public void prepareCommandStationDriveMessage() throws ProtocolException {
        // FE 0B 00 39 64 03 00 01 84 00 00 00 00 DC FE
        byte[] message = new byte[] { 0x0B, 0x00, 0x39, 0x64, 0x03, 0x00, 0x01, (byte) 0x84, 0x00, 0x00, 0x00, 0x00 };
        CommandStationDriveMessage driveMessage = new CommandStationDriveMessage(message);
        Assert.assertNotNull(driveMessage);
        LOGGER.info("Prepare pomMessage: {}", driveMessage);

        AddressData locoAddress = new AddressData(3, AddressTypeEnum.LOCOMOTIVE_BACKWARD);

        Assert.assertEquals(driveMessage.getDecoderAddress(), locoAddress);
        Assert.assertEquals(driveMessage.getSpeed(), 0x00);
    }
}
