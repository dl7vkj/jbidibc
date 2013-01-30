package org.bidib.jbidibc;

import org.bidib.jbidibc.exception.PortNotFoundException;
import org.bidib.jbidibc.node.BidibNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.Parameters;

/**
 * This commands reads the value of the specified CV from the specified node.
 *
 */
@Parameters(separators = "=")
public class BoostQuery extends BidibNodeCommand {
	private static final Logger LOGGER = LoggerFactory.getLogger(BoostQuery.class);
	
    public static void main(String[] args) {
    	run(new BoostQuery(), args);
    }
    
    public int execute() {
        int result = 20;

        try {
        	Bidib.open(getPortName());

        	Node node = findNode();

        	if (node != null) {
        		BidibNode bidibNode = Bidib.getNode(node);


        		// TODO verify if the booster query returns the booster state ...
        			/*BoosterState boosterState =*/ bidibNode.boosterQuery();

//        			LOGGER.info("Booster state: {}", boosterState.name());
        			
        			result = 0;
        	} 
        	else {
        		LOGGER.warn("node with unique id \"" + getNodeIdentifier() + "\" not found");
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
