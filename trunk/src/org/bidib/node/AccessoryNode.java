package org.bidib.node;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.bidib.AddressData;
import org.bidib.Bidib;
import org.bidib.LcConfig;
import org.bidib.LcMacro;
import org.bidib.MessageListener;
import org.bidib.MessageReceiver;
import org.bidib.Node;
import org.bidib.enumeration.LcMacroOperationCode;
import org.bidib.enumeration.LcMacroState;
import org.bidib.enumeration.LcOutputType;
import org.bidib.exception.ProtocolException;
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
    public void timeout(byte[] address, int timeout) {
        if (Arrays.equals(address, getAddr())) {
            Bidib.setTimeout(timeout * 1000);
        }
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
        return ((LcMacroParaResponse) send(new LcMacroParaGetMessage(macroNumber, parameter))).getValue();
    }

    public LcMacro getMacroStep(int macroNumber, int stepNumber) throws IOException, ProtocolException,
            InterruptedException {
        return ((LcMacroResponse) send(new LcMacroGetMessage(macroNumber, stepNumber))).getMacro();
    }

    public LcMacroState handleMacro(int macroNumber, LcMacroOperationCode macroOperationCode) throws IOException,
            ProtocolException, InterruptedException {
        return ((LcMacroStateResponse) send(new LcMacroHandleMessage(macroNumber, macroOperationCode))).getMacroState();
    }

    public void setConfig(LcConfig config) throws IOException, ProtocolException, InterruptedException {
        if (config != null) {
            send(new LcConfigSetMessage(config));
        }
    }

    public LcMacro setMacro(LcMacro macro) throws IOException, ProtocolException, InterruptedException {
        return ((LcMacroResponse) send(new LcMacroSetMessage(macro))).getMacro();
    }

    public void setMacroParameter(int macroNumber, int parameter, byte... value) throws IOException, ProtocolException,
            InterruptedException {
        send(new LcMacroParaSetMessage(macroNumber, parameter, value));
    }

    public void setOutput(LcOutputType outputType, int outputNumber, int state) throws IOException, ProtocolException,
            InterruptedException {
        send(new LcOutputMessage(outputType, outputNumber, state));
        Bidib.setTimeout(Bidib.DEFAULT_TIMEOUT);
    }
}
