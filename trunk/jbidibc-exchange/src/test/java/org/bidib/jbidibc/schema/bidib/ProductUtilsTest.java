package org.bidib.jbidibc.schema.bidib;

import org.bidib.jbidibc.utils.NodeUtils;
import org.junit.Assert;
import org.testng.annotations.Test;

public class ProductUtilsTest {

    @Test
    public void isLightControl() {
        long uniqueId = 0x05340d6B901234L;
        Assert.assertTrue(ProductUtils.isLightControl(uniqueId));
        Assert.assertFalse(ProductUtils.isOneControl(uniqueId));
    }

    @Test
    public void isMobaList() {
        long uniqueId = 0x05000D6C003300L;
        Assert.assertTrue(ProductUtils.isMobaList(uniqueId));
        Assert.assertFalse(ProductUtils.isLightControl(uniqueId));
    }

    @Test
    public void isOneDMX() {
        long uniqueId = 0x05340D73901235L;
        Assert.assertTrue(ProductUtils.isOneDMX(uniqueId));
        Assert.assertFalse(ProductUtils.isLightControl(uniqueId));
    }

    @Test
    public void isOneControl() {
        long uniqueId = 0x05340D75901235L;
        Assert.assertTrue(ProductUtils.isOneControl(uniqueId));
        Assert.assertFalse(ProductUtils.isLightControl(uniqueId));
    }

    @Test
    public void isOneHub() {
        long uniqueId = 0x81000D72006AEAL;
        Assert.assertTrue(ProductUtils.isOneHub(uniqueId));
        Assert.assertFalse(ProductUtils.isOneControl(uniqueId));

        Assert.assertTrue(NodeUtils.hasSubNodesFunctions(uniqueId));
    }

    @Test
    public void isOneOC() {
        // 20:01:31.459 [INFO] org.bidib.wizard.comm.bidib.BidibCommunication [Thread-12] - Magic of new node: 45054,
        // node: Node[version=1,addr=[8],uniqueId=0x40000dcc009dea]

        long uniqueId = 0x040000dcc009deaL;
        Assert.assertTrue(ProductUtils.isOneOC(uniqueId));
        Assert.assertFalse(ProductUtils.isOneControl(uniqueId));
    }

    @Test
    public void isOneServoTurn() {
        long uniqueId = 0x05000D79001234L;
        Assert.assertTrue(ProductUtils.isOneServoTurn(uniqueId));
        Assert.assertFalse(ProductUtils.isOneControl(uniqueId));

        Assert.assertTrue(NodeUtils.hasAccessoryFunctions(uniqueId));
        Assert.assertTrue(NodeUtils.hasSwitchFunctions(uniqueId));
    }

    @Test
    public void isOneDriveTurn() {
        long uniqueId = 0x05000D7A001234L;
        Assert.assertTrue(ProductUtils.isOneDriveTurn(uniqueId));
        Assert.assertFalse(ProductUtils.isOneControl(uniqueId));

        Assert.assertTrue(NodeUtils.hasAccessoryFunctions(uniqueId));
        Assert.assertTrue(NodeUtils.hasSwitchFunctions(uniqueId));
    }

    @Test
    public void isOneBootloader() {
        long uniqueId = 0x00000d700009ebL;
        Assert.assertTrue(ProductUtils.isOneBootloader(uniqueId));
        Assert.assertFalse(ProductUtils.isOneControl(uniqueId));
    }

}
