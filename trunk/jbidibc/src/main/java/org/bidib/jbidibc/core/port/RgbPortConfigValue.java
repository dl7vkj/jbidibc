package org.bidib.jbidibc.core.port;

public class RgbPortConfigValue implements PortConfigValue<Integer> {
    private final Integer value;

    public RgbPortConfigValue(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }

}
