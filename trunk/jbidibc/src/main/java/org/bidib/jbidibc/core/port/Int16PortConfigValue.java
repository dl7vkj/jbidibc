package org.bidib.jbidibc.core.port;

public class Int16PortConfigValue implements PortConfigValue<Integer> {

    private final Integer value;

    public Int16PortConfigValue(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }

}
