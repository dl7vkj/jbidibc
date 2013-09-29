package org.bidib.jbidibc;

import org.bidib.jbidibc.utils.AccessoryStateUtils;
import org.bidib.jbidibc.utils.ByteUtils;

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
        StringBuffer sb = new StringBuffer("[ ");
        sb.append(getClass().getSimpleName());
        sb.append(", accessoryNumber: ").append(ByteUtils.getInt(accessoryNumber));
        sb.append(", aspect: ").append(ByteUtils.getInt(aspect));
        sb.append(", total: ").append(ByteUtils.getInt(total));
        sb.append(", execute: ").append(ByteUtils.getInt(execute));
        if ((execute & 0x80) == 0x80) {
            sb.append(" => Error detected.");
        }
        else {
            sb.append("[");
            sb.append(AccessoryStateUtils.getOperationResult(execute));
            sb.append("]");
        }

        // calculate the real time ...
        int remainingTime = 0;
        switch (wait & 0x80) {
            case 0x00: // 100ms
                remainingTime = (wait & 0x7F) * 100;
                break;
            default: // 1s
                remainingTime = (wait & 0x7F) * 1000;
                break;
        }
        sb.append(", remaing wait time: ").append(remainingTime).append("ms");
        sb.append("]");
        return sb.toString();
    }

}
