package org.bidib.jbidibc;

import org.bidib.jbidibc.enumeration.LcOutputType;
import org.bidib.jbidibc.exception.PortNotFoundException;
import org.bidib.jbidibc.node.AccessoryNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.Parameters;

/**
 * This commands gets the list of macros from the specified node.
 *
 */
@Parameters(separators = "=")
public class GetMacros extends BidibNodeCommand {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GetMacros.class);
	
    public static void main(String[] args) {
    	
    	run(new GetMacros(), args);
    }
    
    public int execute() {
        int result = 20;

        try {
            Bidib.open(getPortName());

            Node node = findNode();

            if (node != null) {
                AccessoryNode accessoryNode = Bidib.getAccessoryNode(node);

                if (accessoryNode != null) {
                    Feature feature = accessoryNode.getFeature(BidibLibrary.FEATURE_CTRL_MAC_COUNT);
                    int macroCount = feature != null ? feature.getValue() : 0;

                    feature = accessoryNode.getFeature(BidibLibrary.FEATURE_CTRL_MAC_SIZE);

                    int macroLength = feature != null ? feature.getValue() : 0;

                    for (int macroNumber = 0; macroNumber < macroCount; macroNumber++) {
                        int stepNumber = 0;

                        LOGGER.info("Macro " + macroNumber + ":");
                        LOGGER.info("\tcycles: "
                                + accessoryNode
                                        .getMacroParameter(macroNumber, BidibLibrary.BIDIB_MACRO_PARA_REPEAT));
                        LOGGER.info("\tspeed: "
                                + accessoryNode.getMacroParameter(macroNumber,
                                        BidibLibrary.BIDIB_MACRO_PARA_SLOWDOWN));
                        LOGGER.info("\tsteps:");
                        for (;;) {
                            final LcMacro macroStep = accessoryNode.getMacroStep(macroNumber,
                                    stepNumber++);

                            if (macroStep.getOutputType() == LcOutputType.END_OF_MACRO || stepNumber > macroLength) {
                                break;
                            }
                            LOGGER.info("\t\t" + stepNumber + ". " + macroStep);
                        }
                    }
                    result = 0;
                } else {
                	LOGGER.error("node with unique id \"" + getNodeIdentifier() + "\" doesn't have macros");
                }
            } else {
            	LOGGER.error("node with unique id \"" + getNodeIdentifier() + "\" not found");
            }
        } 
        catch(PortNotFoundException ex) {
        	LOGGER.error("The provided port was not found: " + ex.getMessage()+". Verify that the BiDiB device is connected.", ex);
        }            
        catch (Exception ex) {
            LOGGER.error("Execute command failed.", ex);
        }
        
        return result;
    }
}
