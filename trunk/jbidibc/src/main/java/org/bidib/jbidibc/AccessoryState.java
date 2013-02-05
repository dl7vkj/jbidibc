package org.bidib.jbidibc;

public class AccessoryState {
    private final byte accessoryNumber;

    private final byte aspect;

    private final byte total;

    private final byte execute;

    private final byte wait;

    public AccessoryState(byte accessoryNumber, byte aspect, byte total, byte execute, byte wait) {
        this.accessoryNumber = accessoryNumber;
        this.aspect = aspect;
        this.total = total;
        this.execute = execute;
        this.wait = wait;
    }

    public byte getAccessoryNumber() {
        return accessoryNumber;
    }

    public byte getAspect() {
        return aspect;
    }

    public byte getTotal() {
        return total;
    }

    public byte getExecute() {
        return execute;
    }

    public byte getWait() {
        return wait;
    }

    public String toString() {
        return getClass().getSimpleName() + "[accessoryNumber=" + accessoryNumber + ",aspect=" + aspect + ",total="
            + total + ",execute=" + execute + ",wait=" + wait + "]";
    }
}
