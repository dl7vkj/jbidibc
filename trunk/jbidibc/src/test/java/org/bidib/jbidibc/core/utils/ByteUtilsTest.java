package org.bidib.jbidibc.core.utils;

import org.bidib.jbidibc.core.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ByteUtilsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ByteUtilsTest.class);

    @Test
    public void convertLongToUniqueId() {
        long uniqueId = 0xd2000d68000036L;
        byte[] uniqueIdentifier = ByteUtils.convertLongToUniqueId(uniqueId);

        Assert.assertEquals(new byte[] { (byte) 0xd2, 0x00, 0x0d, 0x68, 0x00, 0x00, 0x36 }, uniqueIdentifier);
    }

    @Test
    public void convertLongToUniqueId2() {
        long uniqueId = 0x05000d6b0069eaL;
        byte[] uniqueIdentifier = ByteUtils.convertLongToUniqueId(uniqueId);

        Assert.assertEquals(new byte[] { (byte) 0x05, 0x00, 0x0d, 0x6b, 0x00, 0x69, (byte) 0xea }, uniqueIdentifier);
    }

    @Test
    public void getVidPidFromUniqueId() {
        long uniqueId = 0xd2000d68000036L;
        byte[] uniqueIdentifier = ByteUtils.getVidPidFromUniqueId(uniqueId);

        Assert.assertEquals(new byte[] { (byte) 0xd2, 0x00, 0x0d, 0x68, 0x00, 0x00, 0x36 }, uniqueIdentifier);
    }

    @Test
    public void getVidPidFromUniqueId2() {
        long uniqueId = 0x05000d6b0069eaL;
        byte[] uniqueIdentifier = ByteUtils.getVidPidFromUniqueId(uniqueId);

        Assert.assertEquals(new byte[] { (byte) 0x05, 0x00, 0x0d, 0x6b, 0x00, 0x69, (byte) 0xea }, uniqueIdentifier);
    }

    @Test
    public void getClassIdFromUniqueId() {
        long uniqueId = 0xd2000d68000036L;
        int classId = ByteUtils.getClassIdFromUniqueId(uniqueId);
        LOGGER.info("ClassId: {}", classId);
        Assert.assertEquals(0xD2, classId);
    }

    @Test
    public void getClassIdFromUniqueId2() {
        long uniqueId = 0x05000d6b0069eaL;
        int classId = ByteUtils.getClassIdFromUniqueId(uniqueId);
        LOGGER.info("ClassId: {}", classId);
        Assert.assertEquals(0x05, classId);
    }

    @Test
    public void getDWORD() {
        byte[] sample = new byte[] { 0x01, 0x02, 0x03, 0x04 };
        int dword = ByteUtils.getDWORD(sample);
        LOGGER.info("Converted dword: {}", dword);

        Assert.assertEquals(0x04030201, dword);
    }

    @Test
    public void getDWORD2() {
        byte[] sample = new byte[] { (byte) 0xF1, 0x02, 0x03, (byte) 0xF4 };
        int dword = ByteUtils.getDWORD(sample);
        LOGGER.info("Converted dword: {}", dword);

        Assert.assertEquals(0xF40302F1, dword);
    }

    @Test
    public void toDWORD() {
        byte[] sample = new byte[] { 0x01, 0x02, 0x03, 0x04 };
        int dword = 0x04030201;
        byte[] test = ByteUtils.toDWORD(dword);
        LOGGER.info("Converted test: {}", test);

        Assert.assertEquals(sample, test);
    }

    @Test
    public void toDWORD2() {
        byte[] sample = new byte[] { (byte) 0xF1, 0x02, 0x03, (byte) 0xF4 };
        int dword = 0xF40302F1;
        byte[] test = ByteUtils.toDWORD(dword);
        LOGGER.info("Converted test: {}", test);

        Assert.assertEquals(sample, test);
    }

    @Test
    public void byteToHexTest() {
        int value = 0xE0;
        String hex = ByteUtils.byteToHex(value);
        LOGGER.info("Converted hex: {}", hex);

        Assert.assertEquals("E0".toCharArray(), hex.toCharArray());
    }

    @Test
    public void isBitSetEqualIntTest() {
        int cvValue = 0x08;
        Assert.assertTrue(ByteUtils.isBitSetEqual(cvValue, 1, 3));
        Assert.assertFalse(ByteUtils.isBitSetEqual(cvValue, 0, 3));
        Assert.assertFalse(ByteUtils.isBitSetEqual(cvValue, 1, 2));
        Assert.assertFalse(ByteUtils.isBitSetEqual(cvValue, 1, 4));

        int cvValue2 = 0x28;
        Assert.assertTrue(ByteUtils.isBitSetEqual(cvValue2, 1, 5));
        Assert.assertFalse(ByteUtils.isBitSetEqual(cvValue2, 0, 5));
        Assert.assertFalse(ByteUtils.isBitSetEqual(cvValue2, 1, 4));
        Assert.assertFalse(ByteUtils.isBitSetEqual(cvValue2, 1, 6));
    }

    @Test
    public void isBitSetEqualByteTest() {
        byte cvValue = 0x08;
        Assert.assertTrue(ByteUtils.isBitSetEqual(cvValue, 1, 3));
        Assert.assertFalse(ByteUtils.isBitSetEqual(cvValue, 0, 3));
        Assert.assertFalse(ByteUtils.isBitSetEqual(cvValue, 1, 2));
        Assert.assertFalse(ByteUtils.isBitSetEqual(cvValue, 1, 4));

        byte cvValue2 = 0x28;
        Assert.assertTrue(ByteUtils.isBitSetEqual(cvValue2, 1, 5));
        Assert.assertFalse(ByteUtils.isBitSetEqual(cvValue2, 0, 5));
        Assert.assertFalse(ByteUtils.isBitSetEqual(cvValue2, 1, 4));
        Assert.assertFalse(ByteUtils.isBitSetEqual(cvValue2, 1, 6));
    }

    @Test
    public void getBitTest() {
        byte cvValue = 0x08;
        Assert.assertEquals(1, ByteUtils.getBit(cvValue, 3));
        Assert.assertEquals(0, ByteUtils.getBit(cvValue, 2));
        Assert.assertEquals(0, ByteUtils.getBit(cvValue, 4));
    }

    @Test
    public void getBitIntTest() {
        int cvValue = 0x08;
        Assert.assertEquals(1, ByteUtils.getBit(cvValue, 3));
        Assert.assertEquals(0, ByteUtils.getBit(cvValue, 2));
        Assert.assertEquals(0, ByteUtils.getBit(cvValue, 4));

        int cvValue2 = 237;
        Assert.assertEquals(1, ByteUtils.getBit(cvValue2, 3));
    }
}
