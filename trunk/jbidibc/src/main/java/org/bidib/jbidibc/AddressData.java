package org.bidib.jbidibc;

import java.io.ByteArrayOutputStream;

import org.bidib.jbidibc.enumeration.AddressTypeEnum;
import org.bidib.jbidibc.utils.ByteUtils;

public class AddressData {
    private final int address;

    private final AddressTypeEnum type;

    public AddressData(int address, AddressTypeEnum type) {
        this.address = address;
        this.type = type;
    }

    public int getAddress() {
        return address;
    }

    public AddressTypeEnum getType() {
        return type;
    }

    public String toString() {
        return getClass().getSimpleName() + "[address=" + address + ",type=" + type + "]";
    }

    public void writeToStream(ByteArrayOutputStream out) {
        int address = getAddress();
        if (address > 0) {
            // write address
            out.write(ByteUtils.getLowByte(address));
            byte highAddr = ByteUtils.getHighByte(address);
            switch (getType()) {
                case LOCOMOTIVE_BACKWARD:
                    highAddr = (byte) (highAddr & 0x3F);
                    highAddr = (byte) (highAddr | (1 << 7));
                    break;
                case ACCESSORY:
                    highAddr = (byte) (highAddr & 0x3F);
                    highAddr = (byte) (highAddr | (1 << 6));
                    break;
                case EXTENDED_ACCESSORY:
                    highAddr = (byte) (highAddr | (1 << 6));
                    highAddr = (byte) (highAddr | (1 << 7));
                    break;
                default:
                    highAddr = (byte) (highAddr & 0x3F);
                    break;
            }
            out.write(highAddr);
        }
        else {
            out.write((byte) 0);
            out.write((byte) 0);
        }
    }
}
