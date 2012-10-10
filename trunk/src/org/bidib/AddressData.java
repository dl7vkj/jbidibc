package org.bidib;

import org.bidib.enumeration.AddressTypeEnum;

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
}
