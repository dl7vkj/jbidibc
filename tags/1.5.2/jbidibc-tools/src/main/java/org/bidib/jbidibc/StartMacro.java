package org.bidib.jbidibc;

import org.bidib.jbidibc.enumeration.LcMacroOperationCode;
import org.bidib.jbidibc.exception.PortNotFoundException;
import org.bidib.jbidibc.node.AccessoryNode;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

/**
 * This commands starts the macro on the specified node.
 *
 */
@Parameters(separators = "=")
public class StartMacro extends BidibNodeCommand {
    @Parameter(names = { "-macro" }, description = "The macro number", required = true)
    private int macroNumber;

    public static void main(String[] args) {
        run(new StartMacro(), args);
    }

    public int execute() {
        int result = 20;

        try {
            Bidib.getInstance().open(getPortName());

            Node node = findNode();

            if (node != null) {
                AccessoryNode accessoryNode = Bidib.getInstance().getAccessoryNode(node);

                if (accessoryNode != null) {
                    accessoryNode.handleMacro(macroNumber, LcMacroOperationCode.START);
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
