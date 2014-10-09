package org.bidib.jbidibc;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.bidib.jbidibc.core.Context;
import org.bidib.jbidibc.exception.PortNotFoundException;
import org.bidib.jbidibc.exception.PortNotOpenedException;
import org.bidib.jbidibc.node.listener.TransferListener;
import org.bidib.jbidibc.serial.Bidib;
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

    @Parameter(names = { "-port" }, description = "Port to use, e.g. COM1", required = true)
    private String portName;

    private Set<TransferListener> transferListeners = new LinkedHashSet<TransferListener>();

    protected String getPortName() {
        return portName;
    }

    protected BidibCommand() {
        // redirect System.out and System.error calls to SLF4J
        SysOutOverSLF4J.sendSystemOutAndErrToSLF4J(LogLevel.INFO, LogLevel.WARN);
    }

    protected void openPort(String portName, Context context) throws PortNotFoundException, PortNotOpenedException {

        transferListeners.add(new TransferListener() {

            @Override
            public void sendStopped() {
            }

            @Override
            public void sendStarted() {
            }

            @Override
            public void receiveStopped() {
            }

            @Override
            public void receiveStarted() {
            }
        });

        Bidib.getInstance().open(portName, new ConnectionListener() {
            @Override
            public void opened(String port) {
            }

            @Override
            public void closed(String port) {
            }
        }, Collections.<NodeListener> emptySet(), Collections.<MessageListener> emptySet(), transferListeners, context);
    }

    /**
     * Execute the command
     * 
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
            LOGGER.warn("Execution of " + command.getClass().getSimpleName() + " command failed: " + ex.getMessage());
            StringBuilder sb = new StringBuilder();
            jc.usage(sb);
            LOGGER.warn(sb.toString());
        }
        System.exit(result);
    }

}
