package org.bidib.jbidibc.core;

public class Confidence {
    private int freeze = 0;

    private int signal = 0;

    private int valid = 0;

    public Confidence(int valid, int freeze, int signal) {
        this.valid = valid;
        this.freeze = freeze;
        this.signal = signal;
    }

    public int getFreeze() {
        return freeze;
    }

    public int getSignal() {
        return signal;
    }

    public int getValid() {
        return valid;
    }
}
