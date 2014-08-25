package org.bidib.jbidibc.node;

import java.util.LinkedList;
import java.util.List;

import org.bidib.jbidibc.AccessoryState;
import org.bidib.jbidibc.LcMacro;
import org.bidib.jbidibc.LcMacroParaValue;
import org.bidib.jbidibc.MessageReceiver;
import org.bidib.jbidibc.enumeration.LcMacroOperationCode;
import org.bidib.jbidibc.enumeration.LcMacroState;
import org.bidib.jbidibc.enumeration.LcMappingPortType;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.exception.ProtocolNoAnswerException;
import org.bidib.jbidibc.message.AccessoryGetMessage;
import org.bidib.jbidibc.message.AccessoryParaGetMessage;
import org.bidib.jbidibc.message.AccessoryParaResponse;
import org.bidib.jbidibc.message.AccessoryParaSetMessage;
import org.bidib.jbidibc.message.AccessorySetMessage;
import org.bidib.jbidibc.message.BidibCommand;
import org.bidib.jbidibc.message.BidibMessage;
import org.bidib.jbidibc.message.LcMacroGetMessage;
import org.bidib.jbidibc.message.LcMacroHandleMessage;
import org.bidib.jbidibc.message.LcMacroParaResponse;
import org.bidib.jbidibc.message.LcMacroParaSetMessage;
import org.bidib.jbidibc.message.LcMacroResponse;
import org.bidib.jbidibc.message.LcMacroSetMessage;
import org.bidib.jbidibc.message.LcMacroStateResponse;
import org.bidib.jbidibc.utils.AccessoryStateUtils;
import org.bidib.jbidibc.utils.ByteUtils;
import org.bidib.jbidibc.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessoryNode extends DeviceNode {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccessoryNode.class);

    // TODO check if the accessory node can be handled the same way as BoosterNode and CommandStationNode

    AccessoryNode(byte[] addr, MessageReceiver messageReceiver, boolean ignoreWaitTimeout) {
        super(addr, messageReceiver, ignoreWaitTimeout);
    }

    /**
     * Get the accessory parameter of the specified accessory.
     * 
     * @param accessoryNumber
     *            the number of the accessory
     * @param parameter
     *            the parameter to retrieve
     * @return the parameter values of the accessory
     * @throws ProtocolException
     */
    public byte[] getAccessoryParameter(int accessoryNumber, int parameter) throws ProtocolException {
        byte[] result = null;
        BidibMessage response =
            send(new AccessoryParaGetMessage(accessoryNumber, parameter), true, AccessoryParaResponse.TYPE);

        if (response instanceof AccessoryParaResponse) {
            result = ((AccessoryParaResponse) response).getValue();
        }
        return result;
    }

    public byte[] setAccessoryParameter(int accessoryNumber, int parameter, byte[] value) throws ProtocolException {
        // sendNoWait(new AccessoryParaSetMessage(accessoryNumber, parameter, value));
        byte[] result = null;
        BidibMessage response =
            send(new AccessoryParaSetMessage(accessoryNumber, parameter, value), true, AccessoryParaResponse.TYPE);

        if (response instanceof AccessoryParaResponse) {
            result = ((AccessoryParaResponse) response).getValue();
        }
        return result;
    }

    /**
     * Get the accessory state of the specified accessory.
     * 
     * @param accessoryNumber
     *            the number of the accessory
     * @throws ProtocolException
     */
    public void getAccessoryState(int accessoryNumber) throws ProtocolException {
        // response is signaled asynchronously
        sendNoWait(new AccessoryGetMessage(accessoryNumber));
    }

    /**
     * Set the state of the specified accessory.
     * 
     * @param accessoryNumber
     *            the number of the accessory
     * @param aspect
     *            the aspect to set
     * @throws ProtocolException
     */
    public void setAccessoryState(int accessoryNumber, int aspect) throws ProtocolException {
        // response is signaled asynchronously
        sendNoWait(new AccessorySetMessage(accessoryNumber, aspect));
    }

    /**
     * Send the accessory state acknowledgement message for the specified accessory.
     * 
     * @param accessoryState
     *            the accessory state
     * @throws ProtocolException
     */
    public void acknowledgeAccessoryState(AccessoryState accessoryState) throws ProtocolException {
        // TODO check if we must handle this differently ... currently auto-acknowledge new state
        LOGGER.info("Accessory state change notification was received: {}", accessoryState);
        if (!AccessoryStateUtils.hasError(accessoryState.getExecute())) {
            int accessoryNumber = ByteUtils.getInt(accessoryState.getAccessoryNumber());
            byte aspect = accessoryState.getAspect();
            LOGGER.info("Acknowledge the accessory state change for accessory number: {}, aspect: {}", accessoryNumber,
                ByteUtils.getInt(aspect));
            // send acknowledge
            // TODO check how this works ... currently does not work correct ...
            // setAccessoryState(accessoryNumber, aspect);

            // get the errors, see 4.6.4. Uplink: Messages for accessory functions
            // TODO verify what happens exactly before enable this ...
            getAccessoryState(accessoryNumber);
        }
        else {
            LOGGER.warn("An accessory error was detected: {}", accessoryState);
        }
    }

    /**
     * Get the macro parameter.
     * 
     * @param macroNumber
     *            the macro numbr
     * @param parameter
     *            the parameter number
     * @return the parameter value
     * @throws ProtocolException
     */
    public LcMacroParaValue getMacroParameter(int macroNumber, int parameter) throws ProtocolException {
        LcMacroParaValue result = null;
        BidibMessage response =
            send(getRequestFactory().createLcMacroParaGet(macroNumber, parameter), true, LcMacroParaResponse.TYPE);

        if (response instanceof LcMacroParaResponse) {
            result = ((LcMacroParaResponse) response).getLcMacroParaValue();
        }
        else {
            LOGGER.warn("No response received for LcMacroParaGetMessage, macroNumber: {}, parameter: {}", macroNumber,
                parameter);
            throw new ProtocolNoAnswerException(String.format(
                "No response received for LcMacroParaGetMessage, macroNumber: %d, parameter: %d", macroNumber,
                parameter));
        }
        return result;
    }

    /**
     * Get the macro parameter.
     * 
     * @param macroNumber
     *            the macro number
     * @param parameters
     *            the parameter numbers
     * @return the list of parameter values
     * @throws ProtocolException
     */
    public List<LcMacroParaValue> getMacroParameters(int macroNumber, int... parameters) throws ProtocolException {
        List<LcMacroParaValue> results = new LinkedList<>();
        List<BidibCommand> messages = new LinkedList<>();
        for (int parameter : parameters) {

            messages.add(getRequestFactory().createLcMacroParaGet(macroNumber, parameter));
        }
        List<BidibMessage> responses = sendBulk(4, messages);

        if (CollectionUtils.hasElements(responses)) {
            int index = 0;
            for (BidibMessage response : responses) {
                if (response instanceof LcMacroParaResponse) {
                    LcMacroParaValue result = ((LcMacroParaResponse) response).getLcMacroParaValue();
                    results.add(result);
                }
                else {
                    LOGGER.warn("No response received for LcMacroParaGetMessage, macroNumber: {}, parameter: {}",
                        macroNumber, parameters[index]);

                    LcMacroParaValue result = new LcMacroParaValue(macroNumber, parameters[index], null);
                    results.add(result);
                    throw new ProtocolNoAnswerException(String.format(
                        "No response received for LcMacroParaGetMessage, macroNumber: %d, parameter: %d", macroNumber,
                        parameters[index]));
                }

                index++;
            }
        }
        return results;
    }

    /**
     * Get the macro step with the specified step number.
     * 
     * @param macroNumber
     *            the number of the macro
     * @param stepNumber
     *            the number of the step
     * @return the macro step
     * @throws ProtocolException
     */
    public LcMacro getMacroStep(int macroNumber, int stepNumber) throws ProtocolException {
        LcMacro result = null;
        BidibMessage response = send(new LcMacroGetMessage(macroNumber, stepNumber), true, LcMacroResponse.TYPE);

        if (response instanceof LcMacroResponse) {
            result = ((LcMacroResponse) response).getMacro();
            LOGGER.info("The returned macro step is: {}", result);
        }
        return result;
    }

    public LcMacroState handleMacro(int macroNumber, LcMacroOperationCode macroOperationCode) throws ProtocolException {
        LOGGER.debug("handle macro, macroNumber: {}, macroOperationCode: {}", macroNumber, macroOperationCode);

        BidibMessage response =
            send(new LcMacroHandleMessage(macroNumber, macroOperationCode), true, LcMacroStateResponse.TYPE);

        LcMacroState result = null;
        if (response instanceof LcMacroStateResponse) {
            result = ((LcMacroStateResponse) response).getMacroState();
            LOGGER.debug("handle macro returned: {}, response: {}", result,
                ((LcMacroStateResponse) response).toExtendedString());
        }
        return result;
    }

    public LcMacro setMacro(LcMacro macro) throws ProtocolException {
        LOGGER.info("Set the macro point: {}", macro);
        BidibMessage response = send(new LcMacroSetMessage(macro), true, LcMacroResponse.TYPE);

        LcMacro result = null;
        if (response instanceof LcMacroResponse) {
            result = ((LcMacroResponse) response).getMacro();
            LOGGER.debug("Set macro returned: {}", result);
        }
        return result;
    }

    public void setMacroParameter(int macroNumber, int parameter, byte... value) throws ProtocolException {
        LOGGER.debug("Set macro parameter, macroNumber: {}, parameter: {}, value: {}", new Object[] { macroNumber,
            parameter, value });

        BidibMessage response =
            send(new LcMacroParaSetMessage(macroNumber, parameter, value), true, LcMacroParaResponse.TYPE);
        if (response instanceof LcMacroParaResponse) {
            int result = ((LcMacroParaResponse) response).getMacroNumber();
            LOGGER.debug("Set macro parameter returned macronumber: {}", result);
        }
    }

    public void queryPortMapping(LcMappingPortType lcMappingPortType) {

    }
}
