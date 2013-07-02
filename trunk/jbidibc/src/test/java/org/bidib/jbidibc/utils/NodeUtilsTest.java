package org.bidib.jbidibc.utils;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NodeUtilsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeUtilsTest.class);

    @Test
    public void convertAddress() {
        int addr = NodeUtils.convertAddress(new byte[] { 0, 0, 0, 0 });
        Assert.assertEquals(addr, 0);

        int addr2 = NodeUtils.convertAddress(new byte[] { 1, 0, 0, 0 });
        Assert.assertEquals(addr2, 0x01);

        int addr3 = NodeUtils.convertAddress(new byte[] { 1, 1, 0, 0 });
        Assert.assertEquals(addr3, 0x0101);

        int addr4 = NodeUtils.convertAddress(new byte[] { 1, 1, 1, 0 });
        Assert.assertEquals(addr4, 0x010101);

        int addr5 = NodeUtils.convertAddress(new byte[] { 1, 1, 1, 2 });
        Assert.assertEquals(addr5, 0x2010101);
    }

    @Test
    public void hasAccessoryFunctions() {
        BigInteger bigInt = new BigInteger("d7000d68000100", 16);
        long uniqueId = bigInt.longValue();
        boolean hasAccessory = NodeUtils.hasAccessoryFunctions(uniqueId);

        LOGGER.info("hasAccessory functions: {}", hasAccessory);

        Assert.assertTrue(hasAccessory);
    }

    @Test
    public void hasBoosterFunctions() {
        BigInteger bigInt = new BigInteger("d2000d68000100", 16);
        long uniqueId = bigInt.longValue();
        boolean hasBooster = NodeUtils.hasBoosterFunctions(uniqueId);

        LOGGER.info("hasBooster functions: {}", hasBooster);

        Assert.assertTrue(hasBooster);
    }

    @Test
    public void hasCommandStationFunctions() {
        BigInteger bigInt = new BigInteger("d2000d68000100", 16);
        long uniqueId = bigInt.longValue();
        boolean hasCS = NodeUtils.hasCommandStationFunctions(uniqueId);

        LOGGER.info("has CS functions: {}", hasCS);

        Assert.assertTrue(hasCS);
    }

    @Test
    public void hasCommandStationProgrammingFunctions() {
        BigInteger bigInt = new BigInteger("da000d68000100", 16);
        long uniqueId = bigInt.longValue();

        LOGGER.info("Unique Id: {}", NodeUtils.getUniqueIdAsString(uniqueId));

        boolean hasCSP = NodeUtils.hasCommandStationProgrammingFunctions(uniqueId);

        LOGGER.info("has CS programming functions: {}", hasCSP);

        Assert.assertTrue(hasCSP);
    }

    @Test
    public void hasFeedbackFunctions() {
        BigInteger bigInt = new BigInteger("d2000d68000100", 16);
        long uniqueId = bigInt.longValue();

        LOGGER.info("Unique Id: {}", NodeUtils.getUniqueIdAsString(uniqueId));

        boolean hasFeedback = NodeUtils.hasFeedbackFunctions(uniqueId);

        LOGGER.info("hasFeedback functions: {}", hasFeedback);

        Assert.assertTrue(hasFeedback);
    }

    @Test
    public void hasSubNodesFunctions() {
        BigInteger bigInt = new BigInteger("d2000d68000100", 16);
        long uniqueId = bigInt.longValue();

        LOGGER.info("Unique Id: {}", NodeUtils.getUniqueIdAsString(uniqueId));

        boolean hasSubNodes = NodeUtils.hasSubNodesFunctions(uniqueId);

        LOGGER.info("has subnodes functions: {}", hasSubNodes);

        Assert.assertTrue(hasSubNodes);
    }

    @Test
    public void hasSwitchFunctions() {
        BigInteger bigInt = new BigInteger("d3000d68000100", 16);
        long uniqueId = bigInt.longValue();

        LOGGER.info("Unique Id: {}", NodeUtils.getUniqueIdAsString(uniqueId));

        boolean hasSwitch = NodeUtils.hasSwitchFunctions(uniqueId);

        LOGGER.info("hasSwitch functions: {}", hasSwitch);

        Assert.assertTrue(hasSwitch);
    }
}
