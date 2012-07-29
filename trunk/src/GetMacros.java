import org.bidib.Bidib;
import org.bidib.BidibLibrary;
import org.bidib.Feature;
import org.bidib.Node;
import org.bidib.enumeration.LcOutputType;
import org.bidib.node.AccessoryNode;

public class GetMacros extends BidibCommand {
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

                            System.out.println("Macro " + macroNumber + ":");
                            System.out.println("\tcycles: "
                                    + accessoryNode
                                            .getMacroParameter(macroNumber, BidibLibrary.BIDIB_MACRO_PARA_REPEAT));
                            System.out.println("\tspeed: "
                                    + accessoryNode.getMacroParameter(macroNumber,
                                            BidibLibrary.BIDIB_MACRO_PARA_SLOWDOWN));
                            System.out.println("\tsteps:");
                            for (;;) {
                                final org.bidib.LcMacro macroStep = accessoryNode.getMacroStep(macroNumber,
                                        stepNumber++);

                                if (macroStep.getOutputType() == LcOutputType.END_OF_MACRO || stepNumber > macroLength) {
                                    break;
                                }
                                System.out.println("\t\t" + stepNumber + ". " + macroStep);
                            }
                        }
                        result = 0;
                    } else {
                        System.err.println("node with unique id \"" + args[1] + "\" doesn't have macros");
                    }
                } else {
                    System.err.println("node with unique id \"" + args[1] + "\" not found");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("usage: " + GetMacros.class.getSimpleName() + " <COM port> <unique id>");
        }
        System.exit(result);
    }
}
