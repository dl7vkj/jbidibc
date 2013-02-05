package org.bidib.jbidibc.node;

import java.io.IOException;
import java.util.Collection;

import org.bidib.jbidibc.AccessoryState;
import org.bidib.jbidibc.AddressData;
import org.bidib.jbidibc.Bidib;
import org.bidib.jbidibc.LcConfig;
import org.bidib.jbidibc.LcMacro;
import org.bidib.jbidibc.MessageListener;
import org.bidib.jbidibc.MessageReceiver;
import org.bidib.jbidibc.Node;
import org.bidib.jbidibc.enumeration.BoosterState;
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
import org.bidib.jbidibc.message.LcOutputMessage;

public class AccessoryNode extends DeviceNode implements MessageListener {
    AccessoryNode(byte[] addr) {
        super(addr);

        // TODO maybe better to have this injected 
        MessageReceiver.addMessageListener(this);
    }

    @Override
    public void address(byte[] address, int detectorNumber, Collection<AddressData> addressData) {
    }

    @Override
    public void boosterCurrent(byte[] address, int current) {
    }

    @Override
    public void boosterState(byte[] address, BoosterState state) {
    }

    @Override
    public void confidence(byte[] address, int valid, int freeze, int signal) {
    }

    @Override
    public void free(byte[] address, int detectorNumber) {
    }

    @Override
    public void key(byte[] address, int keyNumber, int keyState) {
    }

    @Override
    public void nodeLost(Node node) {
        MessageReceiver.removeMessageListener(this);
    }

    @Override
    public void nodeNew(Node node) {
        MessageReceiver.removeMessageListener(this);
    }

    @Override
    public void occupied(byte[] address, int detectorNumber) {
    }

    @Override
    public void speed(byte[] address, AddressData addressData, int speed) {
    }

    public byte[] getAccessoryParameter(int accessoryNumber, int parameter) throws IOException, ProtocolException,
        InterruptedException {
        byte[] result = null;
        BidibMessage response = send(new AccessoryParaGetMessage(accessoryNumber, parameter));

        if (response instanceof AccessoryParaResponse) {
            result = ((AccessoryParaResponse) response).getValue();
        }
        return result;
    }

    public AccessoryState getAccessoryState(int accessoryNumber) throws IOException, ProtocolException,
        InterruptedException {
        AccessoryState result = null;
        BidibMessage response = send(new AccessoryGetMessage(accessoryNumber));

        if (response instanceof AccessoryStateResponse) {
            result = ((AccessoryStateResponse) response).getAccessoryState();
        }
        return result;
    }

    public LcConfig getConfig(LcOutputType outputType, int outputNumber) throws IOException, ProtocolException,
        InterruptedException {
        LcConfig result = null;
        BidibMessage response = send(new LcConfigGetMessage(outputType, outputNumber));

        if (response instanceof LcConfigResponse) {
            result = ((LcConfigResponse) response).getLcConfig();
        }
        return result;
    }

    public void getKeyState(int keyNumber) throws IOException, ProtocolException, InterruptedException {
        send(new LcKeyMessage(keyNumber), false, null);
    }

    public byte[] getMacroParameter(int macroNumber, int parameter) throws IOException, ProtocolException,
        InterruptedException {
        byte[] result = null;
        BidibMessage response = send(new LcMacroParaGetMessage(macroNumber, parameter));

        if (response instanceof LcMacroParaResponse) {
            result = ((LcMacroParaResponse) response).getValue();
        }
        return result;
    }

    public LcMacro getMacroStep(int macroNumber, int stepNumber) throws IOException, ProtocolException,
        InterruptedException {
        LcMacro result = null;
        BidibMessage response = send(new LcMacroGetMessage(macroNumber, stepNumber));

        if (response instanceof LcMacroResponse) {
            result = ((LcMacroResponse) response).getMacro();
        }
        return result;
    }

    public LcMacroState handleMacro(int macroNumber, LcMacroOperationCode macroOperationCode) throws IOException,
        ProtocolException, InterruptedException {
        LcMacroState result = null;
        BidibMessage response = send(new LcMacroHandleMessage(macroNumber, macroOperationCode));

        if (response instanceof LcMacroStateResponse) {
            result = ((LcMacroStateResponse) response).getMacroState();
        }
        return result;
    }

    public void setAccessoryParameter(int accessoryNumber, int parameter, byte[] value) throws IOException,
        ProtocolException, InterruptedException {
        send(new AccessoryParaSetMessage(accessoryNumber, parameter, value));
    }

    public void setAccessoryState(int accessoryNumber, int aspect) throws IOException, ProtocolException,
        InterruptedException {
        send(new AccessorySetMessage(accessoryNumber, aspect));
    }

    public void setConfig(LcConfig config) throws IOException, ProtocolException, InterruptedException {
        if (config != null) {
            send(new LcConfigSetMessage(config));
        }
    }

    public LcMacro setMacro(LcMacro macro) throws IOException, ProtocolException, InterruptedException {
        LcMacro result = null;
        BidibMessage response = send(new LcMacroSetMessage(macro));

        if (response instanceof LcMacroResponse) {
            result = ((LcMacroResponse) response).getMacro();
        }
        return result;
    }

    public void setMacroParameter(int macroNumber, int parameter, byte... value) throws IOException, ProtocolException,
        InterruptedException {
        send(new LcMacroParaSetMessage(macroNumber, parameter, value));
    }

    public void setOutput(LcOutputType outputType, int outputNumber, int state) throws IOException, ProtocolException,
        InterruptedException {
        send(new LcOutputMessage(outputType, outputNumber, state));
        MessageReceiver.setTimeout(Bidib.DEFAULT_TIMEOUT);
    }
}
