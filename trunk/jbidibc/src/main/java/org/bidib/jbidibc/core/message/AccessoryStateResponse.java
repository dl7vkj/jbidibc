package org.bidib.jbidibc.core.message;

import org.bidib.jbidibc.core.AccessoryState;
import org.bidib.jbidibc.core.BidibLibrary;
import org.bidib.jbidibc.core.exception.ProtocolException;
import org.bidib.jbidibc.core.utils.ByteUtils;

public class AccessoryStateResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_ACCESSORY_STATE;

    AccessoryStateResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 5) {
            throw new ProtocolException("no accessory state received");
        }
    }

    public AccessoryStateResponse(byte[] addr, int num, byte accessoryNum, byte aspect, byte[] value)
        throws ProtocolException {
        this(addr, num, BidibLibrary.MSG_ACCESSORY_STATE, ByteUtils.concat(new byte[] { accessoryNum, aspect }, value));
    }

    public String getName() {
        return "MSG_ACCESSORY_STATE";
    }

    public AccessoryState getAccessoryState() {
        byte[] data = getData();

        return new AccessoryState(data[0], data[1], data[2], data[3], data[4]);
    }

    public String toExtendedString() {
        StringBuffer sb = new StringBuffer("[ ");
        sb.append(super.toString());
        byte[] data = getData();
        boolean errorDetected = false;
        sb.append(", num: ").append(ByteUtils.getInt(data[0]));
        sb.append(", aspect: ").append(ByteUtils.getInt(data[1]));
        sb.append(", total: ").append(ByteUtils.getInt(data[2]));
        sb.append(", execute: ").append(ByteUtils.getInt(data[3]));
        if ((data[3] & 0x80) == 0x80) {
            sb.append(" => Error detected.");
            errorDetected = true;
        }
        else {
            switch (data[3] & 0x01) {
                case 0x00:
                    sb.append(" => Reached end position.");
                    break;
                case 0x01:
                    sb.append(" => End position not yet reached. Check WAIT.");
                    break;
                default:
                    sb.append(" => Unknown.");
                    break;
            }
            switch (data[3] & 0x02) {
                case 0x00:
                    sb.append(" => End position is verified by feedback.");
                    break;
                case 0x02:
                    sb.append(" => No control of end position possible.");
                    break;
                default:
                    sb.append(" => Unknown.");
                    break;
            }
        }

        if (!errorDetected) {
            // calculate the real time ...
            int remainingTime = 0;
            int timeVal = ByteUtils.getInt(data[4]);
            switch (timeVal & 0x80) {
                case 0x00: // 100ms
                    remainingTime = (timeVal & 0x7F) * 100;
                    break;
                default: // 1s
                    remainingTime = (timeVal & 0x7F) * 1000;
                    break;
            }
            sb.append(", remaing wait time: ").append(remainingTime).append("ms");
        }
        else {
            // evaluate the error
            int errorVal = ByteUtils.getInt(data[4]);
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
        return sb.toString();
    }

}
