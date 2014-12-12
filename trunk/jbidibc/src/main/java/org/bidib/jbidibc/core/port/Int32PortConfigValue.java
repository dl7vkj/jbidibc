package org.bidib.jbidibc.core.port;

public class Int32PortConfigValue implements PortConfigValue<Integer> {

    private final Integer value;

    public Int32PortConfigValue(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }

}
