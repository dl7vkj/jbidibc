package org.bidib;

public class Feature {
    private final int type;
    private final int value;

    public Feature(int type, int value) {
        this.type = type;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return getClass().getSimpleName() + "[type=" + type + ",value=" + value + "]";
    }
}
