package org.bidib.jbidibc.node;

import org.bidib.jbidibc.AccessoryState;
import org.bidib.jbidibc.Bidib;
import org.bidib.jbidibc.LcConfig;
import org.bidib.jbidibc.LcMacro;
import org.bidib.jbidibc.MessageReceiver;
import org.bidib.jbidibc.enumeration.LcMacroOperationCode;
import org.bidib.jbidibc.enumeration.LcMacroState;
import org.bidib.jbidibc.enumeration.LcOutputType;
import org.bidib.jbidibc.exception.ProtocolException;
import org.bidib.jbidibc.message.AccessoryGetMessage;
import org.bidib.jbidibc.message.AccessoryParaGetMessage;
import org.bidib.jbidibc.message.AccessoryParaResponse;
import org.bidib.jbidibc.message.AccessoryParaSetMessage;
import org.bidib.jbidibc.message.AccessorySetMessage;
import org.bidib.jbidibc.message.AccessoryStateResponse;
import org.bidib.jbidibc.message.BidibMessage;
import org.bidib.jbidibc.message.LcConfigGetMessage;
import org.bidib.jbidibc.message.LcConfigResponse;
import org.bidib.jbidibc.message.LcConfigSetMessage;
import org.bidib.jbidibc.message.LcKeyMessage;
import org.bidib.jbidibc.message.LcMacroGetMessage;
import org.bidib.jbidibc.message.LcMacroHandleMessage;
import org.bidib.jbidibc.message.LcMacroParaGetMessage;
import org.bidib.jbidibc.message.LcMacroParaResponse;
import org.bidib.jbidibc.message.LcMacroParaSetMessage;
import org.bidib.jbidibc.message.LcMacroResponse;
import org.bidib.jbidibc.message.LcMacroSetMessage;
import org.bidib.jbidibc.message.LcMacroStateResponse;
import org.bidib.jbidibc.message.LcNotAvailableResponse;
import org.bidib.jbidibc.message.LcOutputMessage;
import org.bidib.jbidibc.utils.AccessoryStateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessoryNode extends DeviceNode {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccessoryNode.class);

    // TODO check if the accessory node can be handled the same way as BoosterNode and CommandStationNode

    AccessoryNode(byte[] addr, MessageReceiver messageReceiver) {
        super(addr, messageReceiver);
    }

    public byte[] getAccessoryParameter(int accessoryNumber, int parameter) throws ProtocolException {
        byte[] result = null;
        BidibMessage response =
            send(new AccessoryParaGetMessage(accessoryNumber, parameter), true, AccessoryParaResponse.TYPE);

        if (response instanceof AccessoryParaResponse) {
            result = ((AccessoryParaResponse) response).getValue();
        }
        return result;
    }

    public AccessoryState getAccessoryState(int accessoryNumber) throws ProtocolException {
        AccessoryState result = null;
        BidibMessage response = send(new AccessoryGetMessage(accessoryNumber), true, AccessoryStateResponse.TYPE);

        if (response instanceof AccessoryStateResponse) {
            result = ((AccessoryStateResponse) response).getAccessoryState();
        }
        return result;
    }

    /**
     * Get the configuration of the specified port.
     * @param outputType the port type
     * @param outputNumber the output number
     * @return the configuration of the specified port.
     * @throws ProtocolException
     */
    public LcConfig getConfig(LcOutputType outputType, int outputNumber) throws ProtocolException {
        LcConfig result = null;
        BidibMessage response =
            send(new LcConfigGetMessage(outputType, outputNumber), true, LcConfigResponse.TYPE,
                LcNotAvailableResponse.TYPE);

        if (response instanceof LcConfigResponse) {
            result = ((LcConfigResponse) response).getLcConfig();
        }
        return result;
    }

    public void getKeyState(int keyNumber) throws ProtocolException {
        // response is signaled asynchronously
        sendNoWait(new LcKeyMessage(keyNumber));
    }

    public byte[] getMacroParameter(int macroNumber, int parameter) throws ProtocolException {
        byte[] result = null;
        BidibMessage response = send(new LcMacroParaGetMessage(macroNumber, parameter), true, LcMacroParaResponse.TYPE);

        if (response instanceof LcMacroParaResponse) {
            result = ((LcMacroParaResponse) response).getValue();
        }
        return result;
    }

    public LcMacro getMacroStep(int macroNumber, int stepNumber) throws ProtocolException {
        LcMacro result = null;
        BidibMessage response = send(new LcMacroGetMessage(macroNumber, stepNumber), true, LcMacroResponse.TYPE);

        if (response instanceof LcMacroResponse) {
            result = ((LcMacroResponse) response).getMacro();
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
            LOGGER.debug("handle macro returned: {}, response: {}", result, ((LcMacroStateResponse) response)
                .toExtendedString());
        }
        return result;
    }

    public void setAccessoryParameter(int accessoryNumber, int parameter, byte[] value) throws ProtocolException {
        sendNoWait(new AccessoryParaSetMessage(accessoryNumber, parameter, value));
    }

    public void setAccessoryState(int accessoryNumber, int aspect) throws ProtocolException {
        // response is signaled asynchronously
        sendNoWait(new AccessorySetMessage(accessoryNumber, aspect));
    }

    /**
     * Send the accessory state acknowledgement message for the specified accessory.
     * @param accessoryState the accessory state
     * @throws ProtocolException
     */
    public void acknowledgeAccessoryState(AccessoryState accessoryState) throws ProtocolException {
        // TODO check if we must handle this differently ... currently auto-acknowledge new state
        if (AccessoryStateUtils.hasError(accessoryState.getExecute())) {
            int accessoryNumber = accessoryState.getAccessoryNumber();
            byte aspect = accessoryState.getAspect();
            LOGGER.info("Acknowledge the accessory state change for accessory number: {}, aspect: {}", accessoryNumber,
                aspect);
            setAccessoryState(accessoryNumber, aspect);
        }
    }

    public void setConfig(LcConfig config) throws ProtocolException {
        if (config != null) {
            LOGGER.debug("Send LcConfigSet to node, config: {}", config);

            send(new LcConfigSetMessage(config), true, LcConfigResponse.TYPE, LcNotAvailableResponse.TYPE);
        }
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

    public void setOutput(LcOutputType outputType, int outputNumber, int state) throws ProtocolException {
        LOGGER
            .debug("Set the new output state, type: {}, outputNumber: {}, state: {}", outputType, outputNumber, state);
        sendNoWait(new LcOutputMessage(outputType, outputNumber, state));
        // TODO not sure why this is needed here ...
        getMessageReceiver().setTimeout(Bidib.DEFAULT_TIMEOUT);
    }
}
