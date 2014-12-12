package org.bidib.jbidibc.core.port;

public class BytePortConfigValue implements PortConfigValue<Byte> {

    private final Byte value;

    public BytePortConfigValue(Byte value) {
        this.value = value;
    }

    @Override
    public Byte getValue() {
        return value;
    }

}
