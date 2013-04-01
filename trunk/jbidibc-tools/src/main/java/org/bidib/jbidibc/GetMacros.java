package org.bidib.jbidibc;

import org.bidib.jbidibc.enumeration.LcOutputType;
import org.bidib.jbidibc.exception.PortNotFoundException;
import org.bidib.jbidibc.node.AccessoryNode;

import com.beust.jcommander.Parameters;

/**
 * This commands gets the list of macros from the specified node.
 *
 */
@Parameters(separators = "=")
public class GetMacros extends BidibNodeCommand {

    public static void main(String[] args) {

        run(new GetMacros(), args);
    }

    public int execute() {
        int result = 20;

        try {
            Bidib.getInstance().open(getPortName());

            Node node = findNode();

            if (node != null) {
                AccessoryNode accessoryNode = Bidib.getInstance().getAccessoryNode(node);

                if (accessoryNode != null) {
                    Feature feature = accessoryNode.getFeature(BidibLibrary.FEATURE_CTRL_MAC_COUNT);
                    int macroCount = feature != null ? feature.getValue() : 0;

                    feature = accessoryNode.getFeature(BidibLibrary.FEATURE_CTRL_MAC_SIZE);

                    int macroLength = feature != null ? feature.getValue() : 0;

                    for (int macroNumber = 0; macroNumber < macroCount; macroNumber++) {
                        int stepNumber = 0;

                        System.out.println("Macro " + macroNumber + ":");
                        System.out.println("\tcycles: "
                            + accessoryNode.getMacroParameter(macroNumber, BidibLibrary.BIDIB_MACRO_PARA_REPEAT));
                        System.out.println("\tspeed: "
                            + accessoryNode.getMacroParameter(macroNumber, BidibLibrary.BIDIB_MACRO_PARA_SLOWDOWN));
                        System.out.println("\tsteps:");
                        for (;;) {
                            final LcMacro macroStep = accessoryNode.getMacroStep(macroNumber, stepNumber++);

                            if (macroStep.getOutputType() == LcOutputType.END_OF_MACRO || stepNumber > macroLength) {
                                break;
                            }
                            System.out.println("\t\t" + stepNumber + ". " + macroStep);
                        }
                    }
                    result = 0;
                }
                else {
                    System.err.println("node with unique id \"" + getNodeIdentifier() + "\" doesn't have macros");
                }
            }
            else {
                System.err.println("node with unique id \"" + getNodeIdentifier() + "\" not found");
            }

            Bidib.getInstance().close();

        }
        catch (PortNotFoundException ex) {
            System.err.println("The provided port was not found: " + ex.getMessage()
                + ". Verify that the BiDiB device is connected.");
        }
        catch (Exception ex) {
            System.err.println("Execute command failed: " + ex);
        }
        return result;
    }
}
