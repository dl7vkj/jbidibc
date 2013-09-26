package org.bidib.jbidibc.message;

import org.bidib.jbidibc.AccessoryState;
import org.bidib.jbidibc.BidibLibrary;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.utils.ByteUtils;

public class AccessoryStateResponse extends BidibMessage {
    public static final Integer TYPE = BidibLibrary.MSG_ACCESSORY_STATE;

    AccessoryStateResponse(byte[] addr, int num, int type, byte... data) throws ProtocolException {
        super(addr, num, type, data);
        if (data == null || data.length != 5) {
            throw new ProtocolException("no accessory state received");
        }
    }

    public AccessoryState getAccessoryState() {
        byte[] data = getData();

        return new AccessoryState(data[0], data[1], data[2], data[3], data[4]);
    }

    public String toExtendedString() {
        StringBuffer sb = new StringBuffer("[ ");
        sb.append(super.toString());
        byte[] data = getData();
        sb.append(", num: ").append(ByteUtils.getInt(data[0]));
        sb.append(", aspect: ").append(ByteUtils.getInt(data[1]));
        sb.append(", total: ").append(ByteUtils.getInt(data[2]));
        sb.append(", execute: ").append(ByteUtils.getInt(data[3]));
        if ((data[3] & 0x80) == 0x80) {
            sb.append(" => Error detected.");
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
        sb.append(", wait: ").append(ByteUtils.getInt(data[4]));
        return sb.toString();
    }

}
