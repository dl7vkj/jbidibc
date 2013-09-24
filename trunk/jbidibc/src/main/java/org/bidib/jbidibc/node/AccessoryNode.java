package org.bidib.jbidibc.node;

import java.io.IOException;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessoryNode extends DeviceNode {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccessoryNode.class);

    // TODO check if the accessory node can be handled the same way as BoosterNode and CommandStationNode

    AccessoryNode(byte[] addr, MessageReceiver messageReceiver) {
        super(addr, messageReceiver);
    }

    public byte[] getAccessoryParameter(int accessoryNumber, int parameter) throws IOException, ProtocolException,
        InterruptedException {
        byte[] result = null;
        BidibMessage response =
            send(new AccessoryParaGetMessage(accessoryNumber, parameter), true, AccessoryParaResponse.TYPE);

        if (response instanceof AccessoryParaResponse) {
            result = ((AccessoryParaResponse) response).getValue();
        }
        return result;
    }

    public AccessoryState getAccessoryState(int accessoryNumber) throws IOException, ProtocolException,
        InterruptedException {
        AccessoryState result = null;
        BidibMessage response = send(new AccessoryGetMessage(accessoryNumber), true, AccessoryStateResponse.TYPE);

        if (response instanceof AccessoryStateResponse) {
            result = ((AccessoryStateResponse) response).getAccessoryState();
        }
        return result;
    }

    public LcConfig getConfig(LcOutputType outputType, int outputNumber) throws IOException, ProtocolException,
        InterruptedException {
        LcConfig result = null;
        BidibMessage response = send(new LcConfigGetMessage(outputType, outputNumber), true, LcConfigResponse.TYPE, LcNotAvailableResponse.TYPE);

        if (response instanceof LcConfigResponse) {
            result = ((LcConfigResponse) response).getLcConfig();
        }
        return result;
    }

    public void getKeyState(int keyNumber) throws IOException, ProtocolException, InterruptedException {
        sendNoWait(new LcKeyMessage(keyNumber));
    }

    public byte[] getMacroParameter(int macroNumber, int parameter) throws IOException, ProtocolException,
        InterruptedException {
        byte[] result = null;
        BidibMessage response = send(new LcMacroParaGetMessage(macroNumber, parameter), true, LcMacroParaResponse.TYPE);

        if (response instanceof LcMacroParaResponse) {
            result = ((LcMacroParaResponse) response).getValue();
        }
        return result;
    }

    public LcMacro getMacroStep(int macroNumber, int stepNumber) throws IOException, ProtocolException,
        InterruptedException {
        LcMacro result = null;
        BidibMessage response = send(new LcMacroGetMessage(macroNumber, stepNumber), true, LcMacroResponse.TYPE);

        if (response instanceof LcMacroResponse) {
            result = ((LcMacroResponse) response).getMacro();
        }
        return result;
    }

    public LcMacroState handleMacro(int macroNumber, LcMacroOperationCode macroOperationCode) throws IOException,
        ProtocolException, InterruptedException {
        LOGGER.debug("handle macro, macroNumber: {}, macroOperationCode: {}", macroNumber, macroOperationCode);

        BidibMessage response =
            send(new LcMacroHandleMessage(macroNumber, macroOperationCode), true, LcMacroStateResponse.TYPE);

        LcMacroState result = null;
        if (response instanceof LcMacroStateResponse) {
            result = ((LcMacroStateResponse) response).getMacroState();
            LOGGER.debug("handle macro returned: {}", result);
        }
        return result;
    }

    public void setAccessoryParameter(int accessoryNumber, int parameter, byte[] value) throws IOException,
        ProtocolException, InterruptedException {
        sendNoWait(new AccessoryParaSetMessage(accessoryNumber, parameter, value));
    }

    public void setAccessoryState(int accessoryNumber, int aspect) throws IOException, ProtocolException,
        InterruptedException {
        sendNoWait(new AccessorySetMessage(accessoryNumber, aspect));
    }

    public void setConfig(LcConfig config) throws IOException, ProtocolException, InterruptedException {
        if (config != null) {
            LOGGER.debug("Send LcConfigSet to node, config: {}", config);

            send(new LcConfigSetMessage(config), true, LcConfigResponse.TYPE, LcNotAvailableResponse.TYPE);
        }
    }

    public LcMacro setMacro(LcMacro macro) throws IOException, ProtocolException, InterruptedException {
        LOGGER.debug("Set macro: {}", macro);
        BidibMessage response = send(new LcMacroSetMessage(macro), true, LcMacroResponse.TYPE);

        LcMacro result = null;
        if (response instanceof LcMacroResponse) {
            result = ((LcMacroResponse) response).getMacro();
            LOGGER.debug("Set macro returned: {}", result);
        }
        return result;
    }

    public void setMacroParameter(int macroNumber, int parameter, byte... value) throws IOException, ProtocolException,
        InterruptedException {
        LOGGER.debug("Set macro parameter, macroNumber: {}, parameter: {}, value: {}", new Object[] { macroNumber,
            parameter, value });

        BidibMessage response =
            send(new LcMacroParaSetMessage(macroNumber, parameter, value), true, LcMacroParaResponse.TYPE);
        if (response instanceof LcMacroParaResponse) {
            int result = ((LcMacroParaResponse) response).getMacroNumber();
            LOGGER.debug("Set macro parameter returned macronumber: {}", result);
        }
    }

    public void setOutput(LcOutputType outputType, int outputNumber, int state) throws IOException, ProtocolException,
        InterruptedException {
        LOGGER
            .debug("Set the new output state, type: {}, outputNumber: {}, state: {}", outputType, outputNumber, state);
        sendNoWait(new LcOutputMessage(outputType, outputNumber, state));
        // TODO not sure why this is needed here ...
        getMessageReceiver().setTimeout(Bidib.DEFAULT_TIMEOUT);
    }
}
