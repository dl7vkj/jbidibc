package org.bidib.jbidibc.message;

import java.util.Calendar;

import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SysClockMessage extends BidibCommandMessage {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysClockMessage.class);

    public SysClockMessage(Calendar date, int factor) {
        super(0, BidibLibrary.MSG_SYS_CLOCK, getMinute(date.get(Calendar.MINUTE)), getHour(date
            .get(Calendar.HOUR_OF_DAY)), getDay(date.get(Calendar.DAY_OF_WEEK)), getAccelerationFactor(factor));
    }

    public SysClockMessage(byte[] message) throws ProtocolException {
        super(message);
    }

    public static byte getAccelerationFactor(int factor) {
        return (byte) ((factor | 0xC0) & 0xDF);
    }

    /**
     * @param day
     *            0=every day, 1=SUNDAY,7=SATURDAY
     * 
     * @return day value in BiDiB format
     */
    public static byte getDay(int day) {
        if (day == 0) {
            day = 7;
        }
        else {
            day = (day + 5) % 7;
        }
        byte ret = (byte) ((day | 0x40) & 0x47);

        LOGGER.trace("Get day: {}, returns: {}", day, ret);

        return ret;
    }

    public static byte getHour(int hour) {
        return (byte) ((hour | 0x80) & 0x9F);
    }

    public static byte getMinute(int minute) {
        return (byte) (minute & 0x3F);
    }

    @Override
    public Integer[] getExpectedResponseTypes() {
        return null;
    }
}
