package org.bidib.jbidibc.message;

import java.util.BitSet;

import org.bidib.jbidibc.enumeration.DirectionEnum;
import org.bidib.jbidibc.enumeration.SpeedStepsEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CommandStationDriveMessageTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandStationDriveMessageTest.class);

    @Test
    public void prepareSpeedMessage() {
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
        // functions:
        // speed and
        // light
        // (F1...F4)
        Assert.assertEquals(message.getData()[4], (byte) 51); // speed

        Assert.assertEquals(message.getData()[5], (byte) 27); // FL,F3,F2,F1
    }
}
