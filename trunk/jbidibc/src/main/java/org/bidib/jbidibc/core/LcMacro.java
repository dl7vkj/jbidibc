package org.bidib.jbidibc.core;

import org.bidib.jbidibc.core.enumeration.BidibEnum;
import org.bidib.jbidibc.core.enumeration.LcOutputType;
import org.bidib.jbidibc.core.utils.ByteUtils;

public class LcMacro {
    private final byte macroNumber;

    private final byte stepNumber;

    private final byte delay;

    private final LcOutputType outputType;

    private final byte outputNumber;

    private final BidibEnum status;

    private final byte value;

    // TODO think about changing the param outputType to a new MacroOutputType because macro supports system functions
    // with macro level 2
    public LcMacro(byte macroNumber, byte stepNumber, byte delay, LcOutputType outputType, byte outputNumber,
        BidibEnum status, byte value) {
        this.macroNumber = macroNumber;
        this.stepNumber = stepNumber;
        this.delay = delay;
        this.outputType = outputType;
        this.outputNumber = outputNumber;
        this.status = status;
        this.value = value;
    }

    public byte getMacroNumber() {
        return macroNumber;
    }

    public byte getStepNumber() {
        return stepNumber;
    }

    public byte getDelay() {
        return delay;
    }

    public LcOutputType getOutputType() {
        return outputType;
    }

    public byte getOutputNumber() {
        return outputNumber;
    }

    public BidibEnum getStatus() {
        return status;
    }

    public byte getValue() {
        return value;
    }

    public String toString() {
        return getClass().getSimpleName() + "[macroNumber=" + ByteUtils.getInt(macroNumber) + ",stepNumber="
            + ByteUtils.getInt(stepNumber) + ",delay=" + ByteUtils.getInt(delay) + ",outputType=" + outputType
            + ",outputNumber=" + ByteUtils.getInt(outputNumber) + ",status=" + status + ",value="
            + ByteUtils.getInt(value) + "]";
    }
}
