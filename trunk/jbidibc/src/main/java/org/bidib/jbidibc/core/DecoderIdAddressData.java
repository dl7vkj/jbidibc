package org.bidib.jbidibc.core;

import java.io.ByteArrayOutputStream;

import org.bidib.jbidibc.core.utils.ByteUtils;

public class DecoderIdAddressData {

    private final byte[] didAddress;

    public DecoderIdAddressData(byte[] didAddress) {
        if (didAddress == null || didAddress.length != 5) {
            throw new IllegalArgumentException("The didAddress is mandatory and must have a length of 5 bytes.");
        }
        this.didAddress = didAddress;
    }

    /**
     * @return the didAddress
     */
    public byte[] getDidAddress() {
        return didAddress;
    }

    public String toString() {
        return getClass().getSimpleName() + "[didAddress=" + ByteUtils.bytesToHex(didAddress) + "]";
    }

    public void writeToStream(ByteArrayOutputStream out) {
        // write address
        for (int index = 0; index < 4; index++) {
            out.write(didAddress[index]);
        }
    }

    public byte getManufacturedId() {
        return didAddress[4];
    }
}
