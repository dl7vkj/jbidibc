package org.bidib.jbidibc.core;

import org.bidib.jbidibc.enumeration.LcMappingPortType;

public class LcPortMapping {

    private LcMappingPortType lcMappingPortType;

    private int portCount;

    private int[] portAvailable;

    private int[] portMapping;

    /**
     * Creates a new instance of LcPortMapping.
     * 
     * @param lcMappingPortType
     *            the port type
     * @param portCount
     *            the number of ports
     */
    public LcPortMapping(LcMappingPortType lcMappingPortType, int portCount) {
        this.lcMappingPortType = lcMappingPortType;
        this.portCount = portCount;
    }

    /**
     * @return the LcMappingPortType
     */
    public LcMappingPortType getLcMappingPortType() {
        return lcMappingPortType;
    }

    /**
     * @return the port count
     */
    public int getPortCount() {
        return portCount;
    }

    /**
     * @return the portAvailable
     */
    public int[] getPortAvailable() {
        return portAvailable;
    }

    /**
     * @param portAvailable
     *            the portAvailable to set
     */
    public void setPortAvailable(int[] portAvailable) {
        this.portAvailable = portAvailable;
    }

    /**
     * @return the portMapping
     */
    public int[] getPortMapping() {
        return portMapping;
    }

    /**
     * @param portMapping
     *            the portMapping to set
     */
    public void setPortMapping(int[] portMapping) {
        this.portMapping = portMapping;
    }
}
