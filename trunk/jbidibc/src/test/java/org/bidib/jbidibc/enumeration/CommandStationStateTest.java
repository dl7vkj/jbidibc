package org.bidib.jbidibc.enumeration;

import org.testng.Assert;
import org.testng.annotations.Test;

public class CommandStationStateTest {

    @Test
    public void isOffState() {
        Assert.assertTrue(CommandStationState.isOffState(CommandStationState.OFF));
        Assert.assertTrue(CommandStationState.isOffState(CommandStationState.STOP));
        Assert.assertTrue(CommandStationState.isOffState(CommandStationState.SOFTSTOP));

        Assert.assertFalse(CommandStationState.isOffState(CommandStationState.GO));
        Assert.assertFalse(CommandStationState.isOffState(CommandStationState.PROG));
    }

    @Test
    public void isOnState() {
        Assert.assertTrue(CommandStationState.isOnState(CommandStationState.GO));
        Assert.assertTrue(CommandStationState.isOnState(CommandStationState.GO_IGN_WD));

        Assert.assertFalse(CommandStationState.isOnState(CommandStationState.OFF));
    }

    @Test
    public void isPtProgState() {
        Assert.assertTrue(CommandStationState.isPtProgState(CommandStationState.PROG));
        Assert.assertTrue(CommandStationState.isPtProgState(CommandStationState.PROGBUSY));

        Assert.assertFalse(CommandStationState.isPtProgState(CommandStationState.GO));
    }
}
