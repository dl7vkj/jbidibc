package org.bidib.jbidibc.simulation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.bidib.jbidibc.BidibInterface;
import org.bidib.jbidibc.MessageReceiver;
import org.bidib.jbidibc.simulation.nodes.MasterType;
import org.bidib.jbidibc.simulation.nodes.NodeType;
import org.bidib.jbidibc.simulation.nodes.Simulation;
import org.bidib.jbidibc.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulatorRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulatorRegistry.class);

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
        // start the simulator
        simulator.start();
    }

    public SimulatorNode getSimulator(String nodeAddress) {
        SimulatorNode simulator = null;
        synchronized (simulators) {
            simulator = simulators.get(nodeAddress);
        }
        if (simulator == null) {
            LOGGER.warn("No simulator found for nodeAddress: {}", nodeAddress);
        }
        return simulator;
    }

    private static final String JAXB_PACKAGE = "org.bidib.jbidibc.simulation.nodes";

    private static final String XSD_LOCATION = "/xsd/simulation.xsd";

    public void loadSimulationConfiguration(File simulationConfiguration, BidibInterface bidibInterface) {
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
            SimulatorNode simMaster = createSimulator(master, bidibInterface);

            if (master.getSubNodes() != null && master.getSubNodes().getNode() != null) {
                // TODO add support for hub nodes ...
                for (NodeType subNode : master.getSubNodes().getNode()) {
                    SimulatorNode simNode = createSimulator(subNode, bidibInterface);
                    if (simMaster != null && simMaster instanceof InterfaceNode) {
                        ((InterfaceNode) simMaster).addSubNode(simNode);
                    }

                    simNode.start();
                }
            }

            simMaster.start();
        }
        catch (Exception ex) {
            LOGGER.warn("Load simulation configuration failed.", ex);
        }

    }

    private SimulatorNode createSimulator(NodeType node, BidibInterface bidibInterface) {
        LOGGER.info("Create new simulator for node: {}", node);

        // TODO create and register the nodes in the registry
        String className = node.getClassName();
        String[] nodeAddress = node.getAddress().split("\\.");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        for (String addr : nodeAddress) {
            stream.write(Integer.parseInt(addr));
        }
        byte[] address = stream.toByteArray();
        long uniqueId = ByteUtils.convertUniqueIdToLong(node.getUniqueId());
        try {
            Constructor<SimulatorNode> constructor =
                (Constructor<SimulatorNode>) Class.forName(className).getConstructor(byte[].class, long.class,
                    MessageReceiver.class);
            SimulatorNode simulator = constructor.newInstance(address, uniqueId, bidibInterface.getMessageReceiver());

            if (simulator != null) {
                LOGGER.info("Created new simulator: {}", simulator);
                if (simulator instanceof SwitchingFunctionsNode) {
                    SwitchingFunctionsNode switchingFunctionsNode = (SwitchingFunctionsNode) simulator;
                    switchingFunctionsNode.setPortsConfig(node.getINPUT());
                    switchingFunctionsNode.setPortsConfig(node.getSERVO());
                    switchingFunctionsNode.setPortsConfig(node.getSPORT());
                    switchingFunctionsNode.setPortsConfig(node.getLPORT());
                }
                addSimulator(ByteUtils.bytesToHex(address), simulator);
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
