package org.bidib.message;

import java.util.Calendar;

import org.bidib.BidibLibrary;

public class SysClockMessage extends BidibMessage {
    public SysClockMessage(Calendar date, int factor) {
        super(0, BidibLibrary.MSG_SYS_CLOCK, getMinute(date.get(Calendar.MINUTE)), getHour(date
                .get(Calendar.HOUR_OF_DAY)), getDay(date.get(Calendar.DAY_OF_WEEK)), getAccelerationFactor(factor));
    }

    public static byte getAccelerationFactor(int factor) {
        return (byte) ((factor | 0xC0) & 0xDF);
    }

    public static byte getDay(int day) {
        return (byte) ((((day + 5) % 7) | 0x40) & 0x47);
    }

    public static byte getHour(int hour) {
        return (byte) ((hour | 0x80) & 0x9F);
    }

    public static byte getMinute(int minute) {
        return (byte) (minute & 0x3F);
    }
}
