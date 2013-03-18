package org.bidib.jbidibc;

import org.bidib.jbidibc.enumeration.LcOutputType;

public class LcConfig {
    private final LcOutputType outputType;

    private final int outputNumber;

    private final int value1;

    private final int value2;

    private final int value3;

    private final int value4;

    public LcConfig(LcOutputType outputType, int outputNumber, int value1, int value2, int value3, int value4) {
        this.outputType = outputType;
        this.outputNumber = outputNumber;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
    }

    public LcOutputType getOutputType() {
        return outputType;
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
}
