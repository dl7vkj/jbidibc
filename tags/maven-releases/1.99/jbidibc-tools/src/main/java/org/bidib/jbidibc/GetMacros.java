package org.bidib.jbidibc;

import org.bidib.jbidibc.enumeration.LcOutputType;
import org.bidib.jbidibc.node.AccessoryNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetMacros extends BidibCommand {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GetMacros.class);
	
    public static void main(String[] args) {
        int result = 20;

        if (args.length == 2) {
            try {
                Bidib.open(args[0]);

                Node node = findNode(Long.decode(args[1]));

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
                    	LOGGER.error("node with unique id \"" + args[1] + "\" doesn't have macros");
                    }
                } else {
                	LOGGER.error("node with unique id \"" + args[1] + "\" not found");
                }
            } 
            catch (Exception e) {
                LOGGER.warn("Execute command failed.", e);
            }
        } 
        else {
            LOGGER.error("usage: " + GetMacros.class.getName() + " <COM port> <unique id>");
        }
        System.exit(result);
    }
}
