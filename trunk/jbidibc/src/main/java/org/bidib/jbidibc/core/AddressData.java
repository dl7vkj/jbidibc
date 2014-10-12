package org.bidib.jbidibc.core;

import java.io.ByteArrayOutputStream;

import org.bidib.jbidibc.core.enumeration.AddressTypeEnum;
import org.bidib.jbidibc.core.utils.ByteUtils;

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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AddressData) {
            AddressData other = (AddressData) obj;
            if (address == other.getAddress() && type.equals(other.getType())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
