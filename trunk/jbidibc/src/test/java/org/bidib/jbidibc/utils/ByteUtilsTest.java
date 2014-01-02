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

        Assert
            .assertArrayEquals(new byte[] { 0x00, (byte) 0xd2, 0x00, 0x0d, 0x68, 0x00, 0x00, 0x36 }, uniqueIdentifier);
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
}
