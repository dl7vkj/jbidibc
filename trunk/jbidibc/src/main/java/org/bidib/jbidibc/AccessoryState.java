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

    //    public String toString() {
    //        return getClass().getSimpleName() + "[accessoryNumber=" + ByteUtils.getInt(accessoryNumber) + ",aspect=" + ByteUtils.getInt(aspect) + ",total="
    //            + ByteUtils.getInt(total) + ",execute=" + ByteUtils.getInt(execute) + ",wait=" + ByteUtils.getInt(wait) + "]";
    //    }

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
            sb.append(AccessoryStateUtils.getOperationResult(execute));
            //            switch (execute & 0x01) {
            //                case 0x00:
            //                    sb.append(" => Reached end position.");
            //                    break;
            //                case 0x01:
            //                    sb.append(" => End position not yet reached. Check WAIT.");
            //                    break;
            //                default:
            //                    sb.append(" => Unknown.");
            //                    break;
            //            }
            //            switch (execute & 0x02) {
            //                case 0x00:
            //                    sb.append(" => End position is verified by feedback.");
            //                    break;
            //                case 0x02:
            //                    sb.append(" => No control of end position possible.");
            //                    break;
            //                default:
            //                    sb.append(" => Unknown.");
            //                    break;
            //            }
        }
        sb.append(", wait: ").append(ByteUtils.getInt(wait));
        sb.append("]");
        return sb.toString();
    }

}
