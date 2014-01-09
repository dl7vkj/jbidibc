package org.bidib.jbidibc.utils;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class ByteUtilsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ByteUtilsTest.class);

    @Test
    public void convertLongToUniqueId() {
        long uniqueId = 0xd2000d68000036L;
        byte[] uniqueIdentifier = ByteUtils.convertLongToUniqueId(uniqueId);

        Assert.assertArrayEquals(new byte[] { (byte) 0xd2, 0x00, 0x0d, 0x68, 0x00, 0x00, 0x36 }, uniqueIdentifier);
    }

    @Test
    public void convertLongToUniqueId2() {
        long uniqueId = 0x05000d6b0069eaL;
        byte[] uniqueIdentifier = ByteUtils.convertLongToUniqueId(uniqueId);

        Assert.assertArrayEquals(new byte[] { (byte) 0x05, 0x00, 0x0d, 0x6b, 0x00, 0x69, (byte) 0xea },
            uniqueIdentifier);
    }

    @Test
    public void getVidPidFromUniqueId() {
        long uniqueId = 0xd2000d68000036L;
        byte[] uniqueIdentifier = ByteUtils.getVidPidFromUniqueId(uniqueId);

        Assert.assertArrayEquals(new byte[] { (byte) 0xd2, 0x00, 0x0d, 0x68, 0x00, 0x00, 0x36 }, uniqueIdentifier);
    }

    @Test
    public void getVidPidFromUniqueId2() {
        long uniqueId = 0x05000d6b0069eaL;
        byte[] uniqueIdentifier = ByteUtils.getVidPidFromUniqueId(uniqueId);

        Assert.assertArrayEquals(new byte[] { (byte) 0x05, 0x00, 0x0d, 0x6b, 0x00, 0x69, (byte) 0xea },
            uniqueIdentifier);
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

        Assert.assertArrayEquals(sample, test);
    }

    @Test
    public void toDWORD2() {
        byte[] sample = new byte[] { (byte) 0xF1, 0x02, 0x03, (byte) 0xF4 };
        int dword = 0xF40302F1;
        byte[] test = ByteUtils.toDWORD(dword);
        LOGGER.info("Converted test: {}", test);

        Assert.assertArrayEquals(sample, test);
    }
}
