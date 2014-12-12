package org.bidib.jbidibc.core.port;

import org.bidib.jbidibc.core.enumeration.LcOutputType;

public class ReconfigPortConfigValue implements PortConfigValue<Integer> {
    private final Integer value;

    public ReconfigPortConfigValue(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public LcOutputType getCurrentOutputType() {
        byte type = (byte) (value & 0xFF);
        return LcOutputType.valueOf(type);
    }

    public int getPortMap() {
        int portMap = (value >> 8) & 0xFFFF;
        return portMap;
    }
}
