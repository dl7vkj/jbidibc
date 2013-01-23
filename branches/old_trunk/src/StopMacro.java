import org.bidib.Bidib;
import org.bidib.Node;
import org.bidib.enumeration.LcMacroOperationCode;
import org.bidib.node.AccessoryNode;

public class StopMacro extends BidibCommand {
    public static void main(String[] args) {
        int result = 20;

        if (args.length == 3) {
            try {
                Bidib.open(args[0]);

                Node node = findNode(Long.decode(args[1]));

                if (node != null) {
                    AccessoryNode accessoryNode = Bidib.getAccessoryNode(node);

                    if (accessoryNode != null) {
                        accessoryNode.handleMacro(Integer.decode(args[2]), LcMacroOperationCode.OFF);
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
            System.err.println("usage: " + StopMacro.class.getSimpleName() + " <COM port> <unique id> <macro number>");
        }
        System.exit(result);
    }
}
