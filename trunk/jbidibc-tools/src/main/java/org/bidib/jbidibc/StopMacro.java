package org.bidib.jbidibc;

import org.bidib.jbidibc.enumeration.LcMacroOperationCode;
import org.bidib.jbidibc.exception.PortNotFoundException;
import org.bidib.jbidibc.node.AccessoryNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

/**
 * This commands stops the macro on the specified node.
 *
 */
@Parameters(separators = "=")
public class StopMacro extends BidibNodeCommand {
	private static final Logger LOGGER = LoggerFactory.getLogger(StopMacro.class);

	@Parameter(names = { "-macro" }, description = "The macro number", required=true)
	private int macroNumber;

	public static void main(String[] args) {
		run(new StopMacro(), args);
	}

	public int execute() {
		int result = 20;

		try {
			Bidib.open(getPortName());

			Node node = findNode();

			if (node != null) {
				AccessoryNode accessoryNode = Bidib.getAccessoryNode(node);

				if (accessoryNode != null) {
					accessoryNode.handleMacro(macroNumber, LcMacroOperationCode.OFF);
					result = 0;
				} else {
					LOGGER.warn("node with unique id \"" + getNodeIdentifier() + "\" doesn't have macros");
				}
			} else {
				LOGGER.warn("node with unique id \"" + getNodeIdentifier() + "\" not found");
			}
		} 
		catch(PortNotFoundException ex) {
			LOGGER.error("The provided port was not found: " + ex.getMessage()+". Verify that the BiDiB device is connected.");
		}
		catch (Exception ex) {
			LOGGER.error("Execute command failed.", ex);
		}
		return result;
	}
}
