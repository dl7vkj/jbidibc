package org.bidib.jbidibc.enumeration;

import org.testng.Assert;
import org.testng.annotations.Test;

public class BoosterStateTest {

    @Test
    public void isOffState() {
        Assert.assertTrue(BoosterState.isOffState(BoosterState.OFF));
        Assert.assertTrue(BoosterState.isOffState(BoosterState.OFF_HERE));
        Assert.assertTrue(BoosterState.isOffState(BoosterState.OFF_HOT));
        Assert.assertTrue(BoosterState.isOffState(BoosterState.OFF_NO_DCC));
        Assert.assertTrue(BoosterState.isOffState(BoosterState.OFF_NO_POWER));
        Assert.assertTrue(BoosterState.isOffState(BoosterState.OFF_SHORT));
        Assert.assertTrue(BoosterState.isOffState(BoosterState.OFF_GO_REQ));
    }

    @Test
    public void isOnState() {
        Assert.assertTrue(BoosterState.isOnState(BoosterState.ON));
        Assert.assertTrue(BoosterState.isOnState(BoosterState.ON_LIMIT));
        Assert.assertTrue(BoosterState.isOnState(BoosterState.ON_HOT));
        Assert.assertTrue(BoosterState.isOnState(BoosterState.ON_STOP_REQ));
        Assert.assertTrue(BoosterState.isOnState(BoosterState.ON_HERE));
    }
}
