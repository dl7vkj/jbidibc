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

    // 8 bit values
    public static final Byte BIDIB_PCFG_NONE = Byte.valueOf((byte) 0);

    public static final Byte BIDIB_PCFG_LEVEL_PORT_ON = Byte.valueOf((byte) 1);

    public static final Byte BIDIB_PCFG_LEVEL_PORT_OFF = Byte.valueOf((byte) 2);

    public static final Byte BIDIB_PCFG_DIMM_UP = Byte.valueOf((byte) 3);

    public static final Byte BIDIB_PCFG_DIMM_DOWN = Byte.valueOf((byte) 4);

    public static final Byte BIDIB_PCFG_DIMM_STRETCH = Byte.valueOf((byte) 5);

    public static final Byte BIDIB_PCFG_OUTPUT_MAP = Byte.valueOf((byte) 6);

    public static final Byte BIDIB_PCFG_CONTINUE = Byte.valueOf((byte) 255);

    // 16 bit values
    public static final Byte BIDIB_PCFG_DIMM_UP_8_8 = Byte.valueOf((byte) 0x43);

    public static final Byte BIDIB_PCFG_DIMM_DOWN_8_8 = Byte.valueOf((byte) 0x44);

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
                    if ((pEnum & 0x80) == 0x80) {
                        // int32
                        int pIntValue = entry.getValue().intValue();
                        baos.write(ByteUtils.toDWORD(pIntValue));
                    }
                    else if ((pEnum & 0x40) == 0x40) {
                        // int16
                        int pIntValue = entry.getValue().intValue();
                        baos.write(ByteUtils.toWORD(pIntValue));
                    }
                    else { // byte
                        byte pValue = entry.getValue().byteValue();
                        baos.write(pValue);
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
