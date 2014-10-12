/**
 * 
 */
package org.bidib.jbidibc.core.enumeration;

/**
 * RxTxCommPortType
 * 
 */
public enum RxTxCommPortType {
    PORT_SERIAL(1), PORT_PARALLEL(2), PORT_I2C(3), PORT_RS485(4), PORT_RAW(5);

    private int type;

    RxTxCommPortType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    /**
     * Create an RxTxCommPortType.
     * 
     * @param type
     *            numeric value of the RxTxCommPortType
     * 
     * @return RxTxCommPortType
     */
    public static RxTxCommPortType valueOf(int type) {
        RxTxCommPortType result = null;

        for (RxTxCommPortType e : values()) {
            if (e.type == type) {
                result = e;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("cannot map " + type + " to a RxTxCommPortType");
        }
        return result;
    }

}
