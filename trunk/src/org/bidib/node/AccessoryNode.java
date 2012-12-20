package org.bidib.node;

import java.io.IOException;
import java.util.Collection;

import org.bidib.AccessoryState;
import org.bidib.AddressData;
import org.bidib.Bidib;
import org.bidib.LcConfig;
import org.bidib.LcMacro;
import org.bidib.MessageListener;
import org.bidib.MessageReceiver;
import org.bidib.Node;
import org.bidib.enumeration.BoosterState;
import org.bidib.enumeration.LcMacroOperationCode;
import org.bidib.enumeration.LcMacroState;
import org.bidib.enumeration.LcOutputType;
import org.bidib.exception.ProtocolException;
import org.bidib.message.AccessoryGetMessage;
import org.bidib.message.AccessoryParaGetMessage;
import org.bidib.message.AccessoryParaResponse;
import org.bidib.message.AccessoryParaSetMessage;
import org.bidib.message.AccessorySetMessage;
import org.bidib.message.AccessoryStateResponse;
import org.bidib.message.BidibMessage;
import org.bidib.message.LcConfigGetMessage;
import org.bidib.message.LcConfigResponse;
import org.bidib.message.LcConfigSetMessage;
import org.bidib.message.LcKeyMessage;
import org.bidib.message.LcMacroGetMessage;
import org.bidib.message.LcMacroHandleMessage;
import org.bidib.message.LcMacroParaGetMessage;
import org.bidib.message.LcMacroParaResponse;
import org.bidib.message.LcMacroParaSetMessage;
import org.bidib.message.LcMacroResponse;
import org.bidib.message.LcMacroSetMessage;
import org.bidib.message.LcMacroStateResponse;
import org.bidib.message.LcOutputMessage;

public class AccessoryNode extends DeviceNode implements MessageListener {
    AccessoryNode(byte[] addr) {
        super(addr);
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
        send(new LcKeyMessage(keyNumber), false);
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
