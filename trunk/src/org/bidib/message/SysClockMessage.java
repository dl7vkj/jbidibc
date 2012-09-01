package org.bidib.message;

import java.util.Calendar;

import org.bidib.BidibLibrary;

public class SysClockMessage extends BidibMessage {
    public SysClockMessage(Calendar date, int factor) {
        super(0, BidibLibrary.MSG_SYS_CLOCK, getMinute(date), getHour(date), getDay(date), (byte) (factor & 0xDF));
    }

    public static byte getMinute(Calendar date) {
        return (byte) (date.get(Calendar.MINUTE) & 0x3F);
    }

    public static byte getHour(Calendar date) {
        return (byte) (date.get(Calendar.HOUR_OF_DAY) & 0x9F);
    }

    public static byte getDay(Calendar date) {
        return (byte) (((date.get(Calendar.DAY_OF_WEEK) + 5) % 7) & 0x47);
    }
}
