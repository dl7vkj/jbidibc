package org.bidib.jbidibc.utils;

import org.bidib.jbidibc.LcMacro;
import org.bidib.jbidibc.enumeration.AnalogPortEnum;
import org.bidib.jbidibc.enumeration.BacklightPortEnum;
import org.bidib.jbidibc.enumeration.BidibEnum;
import org.bidib.jbidibc.enumeration.LcOutputType;
import org.bidib.jbidibc.enumeration.LightPortEnum;
import org.bidib.jbidibc.enumeration.ServoPortEnum;
import org.bidib.jbidibc.enumeration.SoundPortEnum;
import org.bidib.jbidibc.enumeration.SwitchPortEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MessageUtilsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageUtilsTest.class);

    @Test
    public void getBacklightPortStatus() {
        LcMacro macro =
            new LcMacro((byte) 0, (byte) 0, (byte) 50, LcOutputType.BACKLIGHTPORT, (byte) 0, BacklightPortEnum.START,
                (byte) 10);
        byte portStatus = MessageUtils.getPortStatus(macro);
        Assert.assertEquals(portStatus, 10);
    }

    @Test
    public void getServoPortStatus() {

        byte portStatus =
            MessageUtils.getPortStatus(new LcMacro((byte) 0, (byte) 0, (byte) 50, LcOutputType.SERVOPORT, (byte) 0,
                ServoPortEnum.START, (byte) 10));
        Assert.assertEquals(portStatus, 10);
    }

    @Test
    public void getSoundPortStatus() {

        byte portStatus =
            MessageUtils.getPortStatus(new LcMacro((byte) 0, (byte) 0, (byte) 50, LcOutputType.SOUNDPORT, (byte) 0,
                SoundPortEnum.PLAY, (byte) 10));
        Assert.assertEquals(portStatus, SoundPortEnum.PLAY.getType());

        portStatus =
            MessageUtils.getPortStatus(new LcMacro((byte) 0, (byte) 0, (byte) 50, LcOutputType.SOUNDPORT, (byte) 0,
                SoundPortEnum.PAUSE, (byte) 10));
        Assert.assertEquals(portStatus, SoundPortEnum.PAUSE.getType());

        portStatus =
            MessageUtils.getPortStatus(new LcMacro((byte) 0, (byte) 0, (byte) 50, LcOutputType.SOUNDPORT, (byte) 0,
                SoundPortEnum.STOP, (byte) 10));
        Assert.assertEquals(portStatus, SoundPortEnum.STOP.getType());
    }

    @Test
    public void getAnalogPortStatus() {

        byte portStatus =
            MessageUtils.getPortStatus(new LcMacro((byte) 0, (byte) 0, (byte) 50, LcOutputType.ANALOGPORT, (byte) 0,
                AnalogPortEnum.START, (byte) 10));
        Assert.assertEquals(portStatus, AnalogPortEnum.START.getType());
    }

    @Test
    public void getSwitchPortStatus() {

        byte portStatus =
            MessageUtils.getPortStatus(new LcMacro((byte) 0, (byte) 0, (byte) 50, LcOutputType.SWITCHPORT, (byte) 0,
                SwitchPortEnum.ON, (byte) 10));
        Assert.assertEquals(portStatus, SwitchPortEnum.ON.getType());

        portStatus =
            MessageUtils.getPortStatus(new LcMacro((byte) 0, (byte) 0, (byte) 50, LcOutputType.SWITCHPORT, (byte) 0,
                SwitchPortEnum.OFF, (byte) 10));
        Assert.assertEquals(portStatus, SwitchPortEnum.OFF.getType());

    }

    @Test
    public void getLightPortStatus() {

        byte portStatus =
            MessageUtils.getPortStatus(new LcMacro((byte) 0, (byte) 0, (byte) 50, LcOutputType.LIGHTPORT, (byte) 0,
                LightPortEnum.ON, (byte) 10));
        Assert.assertEquals(portStatus, LightPortEnum.ON.getType());

        portStatus =
            MessageUtils.getPortStatus(new LcMacro((byte) 0, (byte) 0, (byte) 50, LcOutputType.LIGHTPORT, (byte) 0,
                LightPortEnum.OFF, (byte) 10));
        Assert.assertEquals(portStatus, LightPortEnum.OFF.getType());

        portStatus =
            MessageUtils.getPortStatus(new LcMacro((byte) 0, (byte) 0, (byte) 50, LcOutputType.LIGHTPORT, (byte) 0,
                LightPortEnum.BLINKA, (byte) 10));
        Assert.assertEquals(portStatus, LightPortEnum.BLINKA.getType());

        portStatus =
            MessageUtils.getPortStatus(new LcMacro((byte) 0, (byte) 0, (byte) 50, LcOutputType.LIGHTPORT, (byte) 0,
                LightPortEnum.DOUBLEFLASH, (byte) 10));
        Assert.assertEquals(portStatus, LightPortEnum.DOUBLEFLASH.getType());

        portStatus =
            MessageUtils.getPortStatus(new LcMacro((byte) 0, (byte) 0, (byte) 50, LcOutputType.LIGHTPORT, (byte) 0,
                LightPortEnum.DOWN, (byte) 10));
        Assert.assertEquals(portStatus, LightPortEnum.DOWN.getType());

        portStatus =
            MessageUtils.getPortStatus(new LcMacro((byte) 0, (byte) 0, (byte) 50, LcOutputType.LIGHTPORT, (byte) 0,
                LightPortEnum.UP, (byte) 10));
        Assert.assertEquals(portStatus, LightPortEnum.UP.getType());
    }

    @Test
    public void toPortStatus() {
        byte value = 0;
        for (LcOutputType outputType : LcOutputType.values()) {
            LOGGER.info("Prepare port status for outputType: {}", outputType);

            BidibEnum portStatus = MessageUtils.toPortStatus(outputType, value);
            if (outputType.hasPortStatus()) {
                Assert.assertNotNull(portStatus);
                Assert.assertEquals(portStatus.getType(), 0);
            }
            else {
                Assert.assertNull(portStatus);
            }
        }
    }
}
