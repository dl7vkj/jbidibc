package org.bidib.jbidibc.core;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.bidib.jbidibc.core.enumeration.LcOutputType;
import org.bidib.jbidibc.core.utils.ByteUtils;

public class LcConfig {
    private final LcOutputType outputType;

    private final int outputNumber;

    private final boolean inActive;

    private final int value1;

    private final int value2;

    private final int value3;

    private final int value4;

    public LcConfig(LcOutputType outputType, boolean inActive, int outputNumber, int value1, int value2, int value3,
        int value4) {
        this.outputType = outputType;
        this.inActive = inActive;
        this.outputNumber = outputNumber;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
    }

    public LcOutputType getOutputType() {
        return outputType;
    }

    /**
     * @return the inActive flag
     */
    public boolean isInActive() {
        return inActive;
    }

    public int getOutputNumber() {
        return outputNumber;
    }

    public int getValue1() {
        return value1;
    }

    public int getValue2() {
        return value2;
    }

    public int getValue3() {
        return value3;
    }

    public int getValue4() {
        return value4;
    }

    public byte[] getPortConfig() {
        return new byte[] { ByteUtils.getLowByte(value1), ByteUtils.getLowByte(value2), ByteUtils.getLowByte(value3),
            ByteUtils.getLowByte(value4) };
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
