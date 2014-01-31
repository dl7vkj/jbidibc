package org.bidib.jbidibc.simulation;

import org.bidib.jbidibc.simulation.nodes.PortType;

public interface SwitchingFunctionsNode {
    /**
     * Set the configuration of ports type.
     * @param portType the configuration of the port
     */
    void setPortsConfig(PortType portType);
}
