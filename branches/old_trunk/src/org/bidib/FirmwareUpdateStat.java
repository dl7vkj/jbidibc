package org.bidib;

import org.bidib.enumeration.FirmwareUpdateState;

public class FirmwareUpdateStat {
    private final FirmwareUpdateState state;
    private final int timeout;

    public FirmwareUpdateStat(FirmwareUpdateState state, int timeout) {
        this.state = state;
        this.timeout = timeout;
    }

    public FirmwareUpdateState getState() {
        return state;
    }

    public int getTimeout() {
        return timeout;
    }

    public String toString() {
        return getClass().getSimpleName() + "[state=" + state + ",timeout=" + timeout + "]";
    }
}
