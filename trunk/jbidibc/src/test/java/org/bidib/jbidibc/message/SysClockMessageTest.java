package org.bidib.jbidibc.message;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SysClockMessageTest {

    @Test
    public void getSundayTest() {
        byte sunday = SysClockMessage.getDay(1);

        Assert.assertEquals(0x46, sunday);
    }

    @Test
    public void getMondayTest() {
        byte monday = SysClockMessage.getDay(2);

        Assert.assertEquals(0x40, monday);
    }

    @Test
    public void getTuesdayTest() {
        byte tuesday = SysClockMessage.getDay(3);

        Assert.assertEquals(0x41, tuesday);
    }

    @Test
    public void getWednesdayTest() {
        byte wednesday = SysClockMessage.getDay(4);

        Assert.assertEquals(0x42, wednesday);
    }

    @Test
    public void getThursdayTest() {
        byte thursday = SysClockMessage.getDay(5);

        Assert.assertEquals(0x43, thursday);
    }

    @Test
    public void getFridayTest() {
        byte friday = SysClockMessage.getDay(6);

        Assert.assertEquals(0x44, friday);
    }

    @Test
    public void getSaturdayTest() {
        byte saturday = SysClockMessage.getDay(7);

        Assert.assertEquals(0x45, saturday);
    }
}
