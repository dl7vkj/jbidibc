package org.bidib.jbidibc;

import org.bidib.jbidibc.enumeration.FirmwareUpdateState;

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
