package org.bidib.jbidibc.core;

import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.bidib.jbidibc.core.enumeration.LcOutputType;
import org.bidib.jbidibc.core.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LcConfigX {
    private static final Logger LOGGER = LoggerFactory.getLogger(LcConfigX.class);

    private final LcOutputType outputType;

    private final int outputNumber;

    // the values are stored in a map
    private Map<Byte, Number> values = new LinkedHashMap<>();

    public LcConfigX(LcOutputType outputType, int outputNumber, Map<Byte, Number> values) {
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

    public Map<Byte, Number> getPortConfig() {
        return values;
    }

    public byte[] getCodedPortConfig() {

        byte outputTypeValue = outputType.getType();
        byte portNumber = ByteUtils.getLowByte(outputNumber, 0x7F);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(outputTypeValue);
        baos.write(portNumber);

        // TODO implement
        if (MapUtils.isNotEmpty(values)) {
            try {
                for (Entry<Byte, Number> entry : values.entrySet()) {
                    byte pEnum = entry.getKey();

                    baos.write(pEnum);
                    switch (pEnum & 0x40) {
                        case 0x40: // int
                            int pIntValue = entry.getValue().intValue();
                            baos.write(ByteUtils.toDWORD(pIntValue));
                            break;
                        default: // byte
                            byte pValue = entry.getValue().byteValue();
                            baos.write(pValue);
                            break;
                    }
                }
            }
            catch (Exception ex) {
                LOGGER.warn("Prepare coded port config failed.", ex);
            }
        }
        return baos.toByteArray();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
