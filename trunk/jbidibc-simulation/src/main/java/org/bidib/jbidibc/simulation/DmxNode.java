package org.bidib.jbidibc.simulation;

import org.bidib.jbidibc.simulation.nodes.DmxChannelType;

public interface DmxNode {
    /**
     * Set the configuration of DMX channels.
     * 
     * @param dmxChannelType
     *            the configuration of the dmxChannels
     */
    void setDmxConfig(DmxChannelType dmxChannelType);
}
