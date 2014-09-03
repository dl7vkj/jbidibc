package org.bidib.jbidibc.simulation.nodes;

import org.bidib.jbidibc.Feature;
import org.bidib.jbidibc.message.BidibCommand;
import org.bidib.jbidibc.simulation.SimulatorNode;
import org.bidib.jbidibc.simulation.SwitchingFunctionsNode;

public class Node implements SimulatorNode, SwitchingFunctionsNode {

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void processRequest(final BidibCommand bidibMessage) {
    }

    @Override
    public String getSimulationPanelClass() {
        return null;
    }

    @Override
    public void queryStatus(Class<?> portClass) {
    }

    @Override
    public long getUniqueId() {
        return 0;
    }

    @Override
    public String getAddress() {
        return null;
    }

    @Override
    public String getLocalAddress() {
        return null;
    }

    @Override
    public void setPortsConfig(PortType portType) {
    }

    @Override
    public Feature getFeature(int featureNum) {
        return null;
    }

    @Override
    public void setFeatures(FeaturesType featuresType) {
    }

    @Override
    public void setNodeName(String nodeName) {
    }
}
