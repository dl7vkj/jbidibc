package org.bidib.jbidibc.simulation;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.lang.StringUtils;
import org.bidib.jbidibc.simulation.net.SimulationBidibMessageProcessor;
import org.bidib.jbidibc.simulation.nodes.HubType;
import org.bidib.jbidibc.simulation.nodes.MasterType;
import org.bidib.jbidibc.simulation.nodes.NodeType;
import org.bidib.jbidibc.simulation.nodes.Simulation;
import org.bidib.jbidibc.utils.ByteUtils;
import org.bidib.jbidibc.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulatorRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulatorRegistry.class);

    private static final String JAXB_PACKAGE = "org.bidib.jbidibc.simulation.nodes";

    private static final String XSD_LOCATION = "/xsd/simulation.xsd";

    private static SimulatorRegistry INSTANCE;

    private Map<String, SimulatorNode> simulators;

    private SimulatorRegistry() {
        LOGGER.info("Create instance of SimulatorRegistry.");
        simulators = new HashMap<String, SimulatorNode>();
    }

    public static synchronized SimulatorRegistry getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new SimulatorRegistry();
        }
        return INSTANCE;
    }

    public void removeAll() {
        // TODO remove all simulators and force close all simulation windows
        LOGGER.info("removeAll() will stop the simulators and clear the registry.");

        synchronized (simulators) {
            for (SimulatorNode simulator : simulators.values()) {
                LOGGER.info("Stop the simulator: {}", simulator);
                simulator.stop();
            }

            simulators.clear();
        }
    }

    public Map<String, SimulatorNode> getSimulators() {
        synchronized (simulators) {
            return Collections.unmodifiableMap(simulators);
        }
    }

    public void addSimulator(String nodeAddress, SimulatorNode simulator) {
        LOGGER.info("Add new simulator: {}, nodeAddress: {}", simulator, nodeAddress);
        synchronized (simulators) {
            simulators.put(nodeAddress, simulator);
        }
    }

    public SimulatorNode getSimulator(String nodeAddress) {
        LOGGER.info("Get simulator with nodeAddress: {}", nodeAddress);
        SimulatorNode simulator = null;
        synchronized (simulators) {
            simulator = simulators.get(nodeAddress);
        }
        if (simulator == null) {
            LOGGER.warn("No simulator found for nodeAddress: {}", nodeAddress);
        }
        return simulator;
    }

    public void loadSimulationConfiguration(
        InputStream simulationConfiguration, SimulationBidibMessageProcessor messageReceiver) {
        LOGGER.info("Load simulation configuration from: {}", simulationConfiguration);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_PACKAGE);

            // create a validating unmarshaller
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            StreamSource streamSource = new StreamSource(SimulatorRegistry.class.getResourceAsStream(XSD_LOCATION));
            Schema schema = schemaFactory.newSchema(streamSource);
            unmarshaller.setSchema(schema);

            Simulation simulation = (Simulation) unmarshaller.unmarshal(simulationConfiguration);
            MasterType master = simulation.getMaster();

            LOGGER.info("Fetched master from simulation configuration: {}", master);
            SimulatorNode simMaster = createSimulator(null, master, messageReceiver);

            if (master.getSubNodes() != null && master.getSubNodes().getNode() != null) {
                // create the subnodes of the current node
                createSubNodes(null, master.getSubNodes().getNode(), simMaster, messageReceiver);
            }

            // start the simulator
            simMaster.start();
        }
        catch (Exception ex) {
            LOGGER.warn("Load simulation configuration failed.", ex);
        }

    }

    private void createSubNodes(
        String parentAddress, List<NodeType> subNodes, SimulatorNode simParent,
        SimulationBidibMessageProcessor messageReceiver) {

        for (NodeType subNode : subNodes) {
            LOGGER.info("Create simulator for subNode: {}", subNode);

            // create the simulator for the subnode
            SimulatorNode simNode = createSimulator(parentAddress, subNode, messageReceiver);

            // if the parent is an interface node add it to the parent
            if (simParent != null && simParent instanceof InterfaceNode) {
                LOGGER.info("Adding simulator to simParent: {}, simulator: {}", simParent, simNode);
                ((InterfaceNode) simParent).addSubNode(simNode);
            }

            // start the simulator
            simNode.start();

            if (subNode instanceof HubType && ((HubType) subNode).getSubNodes() != null
                && ((HubType) subNode).getSubNodes().getNode() != null) {

                StringBuffer subNodeAddress = new StringBuffer();
                if (StringUtils.isNotBlank(parentAddress) && !"0".equals(parentAddress)) {
                    LOGGER.info("The parent has address: {}", parentAddress);
                    subNodeAddress.append(parentAddress).append(".");
                }
                subNodeAddress.append(subNode.getAddress());

                createSubNodes(subNodeAddress.toString(), ((HubType) subNode).getSubNodes().getNode(), simNode,
                    messageReceiver);
            }
        }

    }

    private SimulatorNode createSimulator(
        String parentAddress, NodeType node, SimulationBidibMessageProcessor messageReceiver) {
        LOGGER.info("Create new simulator for node: {}, parentAddress: {}", node, parentAddress);

        // create and register the nodes in the registry
        String className = node.getClassName();

        String nodeAddress = node.getAddress().trim();
        byte[] address = new byte[] { Byte.parseByte(nodeAddress) };
        long uniqueId = ByteUtils.convertUniqueIdToLong(node.getUniqueId());
        boolean autoAddFeature = (node.isAutoAddFeature() != null ? node.isAutoAddFeature().booleanValue() : false);

        try {
            Constructor<SimulatorNode> constructor =
                (Constructor<SimulatorNode>) Class.forName(className).getConstructor(byte[].class, long.class,
                    boolean.class, SimulationBidibMessageProcessor.class);
            SimulatorNode simulator = constructor.newInstance(address, uniqueId, autoAddFeature, messageReceiver);

            if (simulator != null) {
                LOGGER.info("Created new simulator: {}", simulator);

                if (node.getFeatures() != null && CollectionUtils.hasElements(node.getFeatures().getFeature())) {
                    simulator.setFeatures(node.getFeatures());
                }

                if (simulator instanceof DmxNode) {
                    DmxNode dmxNode = (DmxNode) simulator;
                    dmxNode.setDmxConfig(node.getDmxChannels());
                }

                if (simulator instanceof SwitchingFunctionsNode) {
                    SwitchingFunctionsNode switchingFunctionsNode = (SwitchingFunctionsNode) simulator;
                    switchingFunctionsNode.setPortsConfig(node.getINPUT());
                    switchingFunctionsNode.setPortsConfig(node.getSERVO());
                    switchingFunctionsNode.setPortsConfig(node.getSPORT());
                    switchingFunctionsNode.setPortsConfig(node.getLPORT());
                    switchingFunctionsNode.setPortsConfig(node.getBACKLIGHT());
                }

                // if the simulator has a parent we must combine the address
                StringBuffer sb = new StringBuffer();
                if (StringUtils.isNotBlank(parentAddress) && !"0".equals(parentAddress)) {
                    LOGGER.info("The parent has address: {}", parentAddress);
                    sb.append(parentAddress).append(".");
                }
                sb.append(nodeAddress);
                LOGGER.info("Adding simulator with address: {}", sb.toString());
                addSimulator(sb.toString(), simulator);
            }
            else {
                LOGGER.warn("No simulator available for configured node: {}", node);
            }

            return simulator;
        }
        catch (Exception ex) {
            LOGGER.warn("Create simulator failed.", ex);
        }
        return null;
    }
}
