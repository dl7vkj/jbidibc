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
import org.bidib.jbidibc.utils.NodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimulatorRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulatorRegistry.class);

    private static SimulatorRegistry INSTANCE;

    private Map<String, Simulator> simulators;

    private SimulatorRegistry() {
        LOGGER.info("Create instance of SimulatorRegistry.");
        simulators = new HashMap<String, Simulator>();
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
            for (Simulator simulator : simulators.values()) {
                LOGGER.info("Stop the simulator: {}", simulator);
                simulator.stop();
            }

            simulators.clear();
        }
    }

    public Map<String, Simulator> getSimulators() {
        synchronized (simulators) {
            return Collections.unmodifiableMap(simulators);
        }
    }

    public void addSimulator(String nodeAddress, Simulator simulator) {
        LOGGER.info("Add new simulator: {}, nodeAddress: {}", simulator, nodeAddress);
        synchronized (simulators) {
            simulators.put(nodeAddress, simulator);
        }
        // start the simulator
        simulator.start();
    }

    public Simulator getSimulator(String nodeAddress) {
        Simulator simulator = null;
        synchronized (simulators) {
            simulator = simulators.get(nodeAddress);
        }
        if (simulator == null) {
            LOGGER.warn("No simulator found for nodeAddress: {}", nodeAddress);
        }
        return simulator;
    }

//    public Simulator createSimulator(long uniqueId, byte[] nodeAddress, MessageReceiver messageReceiver) {
//        int vid = (int) NodeUtils.getVendorId(uniqueId);
//        int pid = (int) NodeUtils.getPid(uniqueId);
//
//        LOGGER.info("Create new simulator with nodeAddress: {}", nodeAddress);
//        Simulator simulator = null;
//        if (vid == 13) {
//            switch (pid) {
//                case 104: // TODO GBMboost master
//                    //                    simulator = new GBMboostMasterSimulator(nodeAddress, messageReceiver);
//                    break;
//                case 107: // TODO light control
//                    //                    simulator = new LightControlSimulator(nodeAddress, messageReceiver);
//                    break;
//                default:
//                    break;
//            }
//        }
//        else if (vid == 62) {
//            switch (pid) {
//                case 144: // Tams BiDiB-Interface mit s88-Bridge
//                    simulator = null;
//                    break;
//                case 151: // Tams MultiDecoder
//                    simulator = null;
//                    break;
//                default:
//                    break;
//            }
//        }
//
//        if (simulator != null) {
//            LOGGER.info("Created new simulator: {}", simulator);
//            addSimulator(ByteUtils.bytesToHex(nodeAddress), simulator);
//        }
//        else {
//            LOGGER.warn("No simulator available for vid: {}, pid: {}", vid, pid);
//        }
//        return simulator;
//    }

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
            createSimulator(master, bidibInterface);
            
            if (master.getSubNodes() != null && master.getSubNodes().getNode() != null) {
            	for (NodeType subNode : master.getSubNodes().getNode()) {
            		createSimulator(subNode, bidibInterface);
            	}
            }
            
        }
        catch (Exception ex) {
            LOGGER.warn("Load simulation configuration failed.", ex);
        }

    }
    
    private void createSimulator(NodeType node, BidibInterface bidibInterface) {
    	LOGGER.info("Create new simulator for node: {}", node);
    	
    	// TODO create and register the nodes in the registry
    	String className = node.getClassName();
    	String[] nodeAddress = node.getAddress().split("\\.");
    	ByteArrayOutputStream stream = new ByteArrayOutputStream();
    	for (String addr : nodeAddress) {
    		stream.write(Integer.parseInt(addr));
    	}
    	byte[] address = stream.toByteArray();
    	
    	try {
    		Constructor<Simulator>[] ctors = (Constructor<Simulator>[]) Class.forName(className).getConstructors();
			Constructor<Simulator> constructor = ctors[0];
			Simulator simulator = constructor.newInstance(address, bidibInterface.getMessageReceiver());
			
			if (simulator != null) {
	            LOGGER.info("Created new simulator: {}", simulator);
	            addSimulator(ByteUtils.bytesToHex(address), simulator);
	        }
	        else {
	            LOGGER.warn("No simulator available for configured node: {}", node);
	        }
		} 
    	catch (Exception ex) {
			LOGGER.warn("Create simulator failed.", ex);
		}
//    	Class.forName(className).getConstructor(byte[], MessageReceiver.class);
    }
}
