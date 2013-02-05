package org.bidib.jbidibc;

import org.bidib.jbidibc.exception.PortNotFoundException;
import org.bidib.jbidibc.node.BidibNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.Parameters;

/**
 * This commands gets the list of nodes from the specified port.
 *
 */
@Parameters(separators = "=")
public class GetNodeList extends BidibCommand {
	private static final Logger LOGGER = LoggerFactory.getLogger(GetNodeList.class);
	
    public static void main(String[] args) {
    	
    	run(new GetNodeList(), args);
    }
    
    public int execute() {
        int result = 20;

        try {
        	Bidib.open(getPortName());

        	BidibNode rootNode = Bidib.getRootNode();
        	int count = rootNode.getNodeCount();

        	for (int index = 1; index <= count; index++) {
        		Node node = rootNode.getNextNode();
        		LOGGER.info("Found node: {}", node);
        	}
        	result = 0;
        }
        catch(PortNotFoundException ex) {
        	LOGGER.error("The provided port was not found: " + ex.getMessage()+". Verify that the BiDiB device is connected.", ex);
        }
        catch (Exception ex) {
        	LOGGER.error("Get list of nodes failed.", ex);
        }
        return result;
    }
}
