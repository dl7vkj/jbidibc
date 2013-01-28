package org.bidib.jbidibc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.org.lidalia.sysoutslf4j.context.LogLevel;
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;

@Parameters(separators = "=")
public abstract class BidibCommand {
	private static final Logger LOGGER = LoggerFactory.getLogger(BidibCommand.class);
	
	@Parameter(names = { "-port" }, description = "Port to use, e.g. COM1", required=true)
	private String portName;

	protected String getPortName() {
		return portName;
	}
	
	protected BidibCommand() {
		// redirect System.out and System.error calls to SLF4J
    	SysOutOverSLF4J.sendSystemOutAndErrToSLF4J(LogLevel.INFO, LogLevel.WARN);
	}
	
	/**
	 * Execute the command
	 * @return the exit code
	 */
	public abstract int execute();
	
	public static void run(BidibCommand command, String[] args) {
		
    	JCommander jc = null;
    	int result = 20;
        try {
	        jc = new JCommander(command);
	        jc.setProgramName(command.getClass().getName());
	        jc.parse(args);
	        
	        result = command.execute();
        }
        catch (ParameterException ex) {
        	LOGGER.warn("Execution of "+command.getClass().getSimpleName()+" command failed: "+ ex.getMessage());
        	StringBuilder sb = new StringBuilder(); 
        	jc.usage(sb);
        	LOGGER.warn(sb.toString());
        }
    	System.exit(result);
	}
	
}
