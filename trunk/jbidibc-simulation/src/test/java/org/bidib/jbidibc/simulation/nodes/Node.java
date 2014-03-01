package org.bidib.jbidibc.simulation.nodes;

import org.bidib.jbidibc.Feature;
import org.bidib.jbidibc.core.Context;
import org.bidib.jbidibc.message.BidibCommand;
import org.bidib.jbidibc.simulation.SimulatorNode;
import org.bidib.jbidibc.simulation.SwitchingFunctionsNode;

public class Node implements SimulatorNode, SwitchingFunctionsNode {

    @Override
    public void start() {
        // TODO Auto-generated method stub

    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub

    }

    @Override
    public void processRequest(final Context context, final BidibCommand bidibMessage) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getSimulationPanelClass() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void queryStatus(Class<?> portClass) {
        // TODO Auto-generated method stub

    }

    @Override
    public long getUniqueId() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getAddress() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setPortsConfig(PortType portType) {
        // TODO Auto-generated method stub

    }

    @Override
    public Feature getFeature(int featureNum) {
        // TODO Auto-generated method stub
        return null;
    }
}
