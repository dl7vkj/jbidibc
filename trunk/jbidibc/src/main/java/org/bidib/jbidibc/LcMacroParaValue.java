package org.bidib.jbidibc;

public class LcMacroParaValue {
    private int macroNumber;

    private int parameter;

    private byte[] value;

    public LcMacroParaValue(int macroNumber, int parameter, byte[] value) {
        this.macroNumber = macroNumber;
        this.parameter = parameter;
        this.value = value;
    }

    /**
     * @return the macroNumber
     */
    public int getMacroNumber() {
        return macroNumber;
    }

    /**
     * @param macroNumber
     *            the macroNumber to set
     */
    public void setMacroNumber(int macroNumber) {
        this.macroNumber = macroNumber;
    }

    /**
     * @return the parameter
     */
    public int getParameter() {
        return parameter;
    }

    /**
     * @param parameter
     *            the parameter to set
     */
    public void setParameter(int parameter) {
        this.parameter = parameter;
    }

    /**
     * @return the value
     */
    public byte[] getValue() {
        return value;
    }
}
