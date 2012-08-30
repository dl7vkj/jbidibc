package org.bidib.message;

import java.util.Calendar;

import org.bidib.BidibLibrary;

public class SysClockMessage extends BidibMessage {
    public SysClockMessage(Calendar date, int factor) {
        //@formatter:off
        super(0,
              BidibLibrary.MSG_SYS_CLOCK,
              (byte) (date.get(Calendar.MINUTE) & 0x3F),
              (byte) (date.get(Calendar.HOUR_OF_DAY) & 0x9F),
              (byte) (((date.get(Calendar.DAY_OF_WEEK) + 5) % 7) & 0x47),
              (byte) (factor & 0xDF));
        //@formatter:on
    }
}
