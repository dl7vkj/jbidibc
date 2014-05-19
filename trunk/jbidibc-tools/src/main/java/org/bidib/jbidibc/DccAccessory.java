package org.bidib.jbidibc;

import org.bidib.jbidibc.enumeration.AccessoryAcknowledge;
import org.bidib.jbidibc.enumeration.ActivateCoilEnum;
import org.bidib.jbidibc.enumeration.AddressTypeEnum;
import org.bidib.jbidibc.enumeration.TimeBaseUnitEnum;
import org.bidib.jbidibc.enumeration.TimingControlEnum;
import org.bidib.jbidibc.exception.InvalidConfigurationException;
import org.bidib.jbidibc.exception.PortNotFoundException;
import org.bidib.jbidibc.node.CommandStationNode;
import org.bidib.jbidibc.serial.Bidib;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

/**
 * This commands reads the value of the specified CV from the specified node.
 * 
 */
@Parameters(separators = "=")
public class DccAccessory extends BidibNodeCommand {
    @Parameter(names = { "-address" }, description = "The DCC decoder address", required = true)
    private int decoderAddress;

    @Parameter(names = { "-activate" }, description = "0: coil-off, 1: coil-on", required = true)
    private int activate;

    @Parameter(names = { "-aspect" }, description = "The aspect to set", required = true)
    private int aspect;

    public static void main(String[] args) {
        run(new DccAccessory(), args);
    }

    public int execute() {
        int result = 20;

        try {
            openPort(getPortName());

            Node node = findNode();

            if (node != null) {
                CommandStationNode bidibNode = Bidib.getInstance().getCommandStationNode(node);

                AddressTypeEnum addressType = AddressTypeEnum.ACCESSORY;
                TimingControlEnum timingControl = TimingControlEnum.OUTPUT_UNIT;
                ActivateCoilEnum activateCoil = (activate != 0 ? ActivateCoilEnum.COIL_ON : ActivateCoilEnum.COIL_OFF);

                TimeBaseUnitEnum timeBaseUnit = TimeBaseUnitEnum.UNIT_100MS;
                byte time = 5;

                AccessoryAcknowledge acknowledge =
                    bidibNode.setAccessory(decoderAddress, addressType, timingControl, activateCoil, aspect,
                        timeBaseUnit, time);

                System.out.println("Acknowledge: " + acknowledge);
            }
            else {
                System.err.println("node with unique id \"" + getNodeIdentifier() + "\" not found");
            }

            Bidib.getInstance().close();

        }
        catch (InvalidConfigurationException ex) {
            System.err.println("Execute command failed: " + ex);
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
