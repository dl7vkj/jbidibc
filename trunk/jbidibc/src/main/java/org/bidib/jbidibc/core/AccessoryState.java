package org.bidib.jbidibc.core;

import org.bidib.jbidibc.core.utils.AccessoryStateUtils;
import org.bidib.jbidibc.core.utils.ByteUtils;

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

    public String getErrorInformation() {
        if ((execute & 0x80) == 0x80) {
            StringBuffer sb = new StringBuffer();
            // evaluate the error
            int errorVal = ByteUtils.getInt(wait);
            boolean moreErrorsAvailable = (errorVal & 0x40) == 0x40;
            sb.append("Error detected: ");

            switch (errorVal & 0x1F) {
                case 0x00: // no more pending errors
                    sb.append("no error (remaining)");
                    break;
                case 0x01:
                    sb.append("command was not executable / unknown command / unknown aspect.");
                    break;
                case 0x02:
                    sb.append("power consumption too high.");
                    break;
                case 0x03:
                    sb.append("power supply below limits, function not garanteed.");
                    break;
                case 0x04:
                    sb.append("fuse blown.");
                    break;
                case 0x05:
                    sb.append("temperature too high.");
                    break;
                case 0x06:
                    sb.append("feedback error / unwanted change of position.");
                    break;
                case 0x07:
                    sb.append("manual control (eg. with local button)");
                    break;
                case 0x08:
                    sb.append("bulb out of order.");
                    break;
                case 0x09:
                    sb.append("servo out of order.");
                    break;
                case 0x3F:
                    sb.append("internal error (eg. selftest, checksum error, ..).");
                    break;
                default:
                    sb.append("Unknown error: ").append(ByteUtils.byteToHex(errorVal));
                    break;
            }

            sb.append(", more errors pending: ").append(moreErrorsAvailable);
            return sb.toString();
        }
        return null;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("[ ");
        sb.append(getClass().getSimpleName());
        sb.append(", accessoryNumber: ").append(ByteUtils.getInt(accessoryNumber));
        sb.append(", aspect: ").append(ByteUtils.getInt(aspect));
        sb.append(", total: ").append(ByteUtils.getInt(total));
        sb.append(", execute: ").append(ByteUtils.getInt(execute));

        boolean errorDetected = false;
        if ((execute & 0x80) == 0x80) {
            sb.append(" => Error detected.");
            errorDetected = true;
        }
        else {
            sb.append("[");
            sb.append(AccessoryStateUtils.getOperationResult(execute));
            sb.append("]");
        }

        if (!errorDetected) {
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
        }
        else {
            // evaluate the error
            int errorVal = ByteUtils.getInt(wait);
            boolean moreErrorsAvailable = (errorVal & 0x40) == 0x40;
            sb.append(", more errors pending: ").append(moreErrorsAvailable).append(", error: ");

            switch (errorVal & 0x1F) {
                case 0x00: // no more pending errors
                    sb.append("no error (remaining)");
                    break;
                case 0x01:
                    sb.append("command was not executable / unknown command / unknown aspect.");
                    break;
                case 0x02:
                    sb.append("power consumption too high.");
                    break;
                case 0x03:
                    sb.append("power supply below limits, function not garanteed.");
                    break;
                case 0x04:
                    sb.append("fuse blown.");
                    break;
                case 0x05:
                    sb.append("temperature too high.");
                    break;
                case 0x06:
                    sb.append("feedback error / unwanted change of position.");
                    break;
                case 0x07:
                    sb.append("manual control (eg. with local button)");
                    break;
                case 0x08:
                    sb.append("bulb out of order.");
                    break;
                case 0x09:
                    sb.append("servo out of order.");
                    break;
                case 0x3F:
                    sb.append("internal error (eg. selftest, checksum error, ..).");
                    break;
                default:
                    sb.append("Unknown error: ").append(ByteUtils.byteToHex(errorVal));
                    break;
            }
        }
        sb.append("]");
        return sb.toString();
    }

}
