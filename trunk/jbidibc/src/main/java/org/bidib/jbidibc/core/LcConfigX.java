package org.bidib.jbidibc.core;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.bidib.jbidibc.core.enumeration.LcOutputType;
import org.bidib.jbidibc.core.port.PortConfigValue;

public class LcConfigX {

    private final LcOutputType outputType;

    private final int outputNumber;

    // the values are stored in a map
    private Map<Byte, PortConfigValue<?>> values = new LinkedHashMap<>();

    public LcConfigX(LcOutputType outputType, int outputNumber, Map<Byte, PortConfigValue<?>> values) {
        this.outputType = outputType;
        this.outputNumber = outputNumber;
        // copy the values to the list
        this.values.putAll(values);
    }

    public LcOutputType getOutputType() {
        return outputType;
    }

    public int getOutputNumber() {
        return outputNumber;
    }

    public Map<Byte, PortConfigValue<?>> getPortConfig() {
        return values;
    }

    public <T> T getPortConfigValue(Byte key) {
        return (T) values.get(key).getValue();
    }

    /**
     * @return the continue marker is contained in the values map
     */
    public boolean isContinueDetected() {
        if (MapUtils.isNotEmpty(values) && values.containsKey(BidibLibrary.BIDIB_PCFG_CONTINUE)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
