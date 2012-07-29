package org.bidib.node;

import java.io.IOException;

import org.bidib.BidibLibrary;
import org.bidib.Feature;
import org.bidib.LcConfig;
import org.bidib.LcMacro;
import org.bidib.enumeration.LcMacroOperationCode;
import org.bidib.enumeration.LcMacroState;
import org.bidib.enumeration.LcOutputType;
import org.bidib.exception.ProtocolException;
import org.bidib.message.BidibMessage;
import org.bidib.message.LcConfigGetMessage;
import org.bidib.message.LcConfigResponse;
import org.bidib.message.LcConfigSetMessage;
import org.bidib.message.LcMacroGetMessage;
import org.bidib.message.LcMacroHandleMessage;
import org.bidib.message.LcMacroParaGetMessage;
import org.bidib.message.LcMacroParaResponse;
import org.bidib.message.LcMacroParaSetMessage;
import org.bidib.message.LcMacroResponse;
import org.bidib.message.LcMacroSetMessage;
import org.bidib.message.LcMacroStateResponse;
import org.bidib.message.LcOutputMessage;


public class AccessoryNode extends DeviceNode {
    AccessoryNode(byte[] addr) {
        super(addr);
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

    public int getMacroParameter(int macroNumber, int parameter) throws IOException, ProtocolException,
            InterruptedException {
        return ((LcMacroParaResponse) send(new LcMacroParaGetMessage(macroNumber, parameter))).getValue();
    }

    public LcMacro getMacroStep(int macroNumber, int stepNumber) throws IOException, ProtocolException,
            InterruptedException {
        return ((LcMacroResponse) send(new LcMacroGetMessage(macroNumber, stepNumber))).getMacro();
    }

    public int getStorableMacroCount() throws IOException, ProtocolException, InterruptedException {
        int result = 0;
        Feature feature = getFeature(BidibLibrary.FEATURE_CTRL_MAC_SAVE);

        if (feature != null) {
            result = feature.getValue();
        }
        return result;
    }

    public LcMacroState handleMacro(int macroNumber, LcMacroOperationCode macroOperationCode) throws IOException,
            ProtocolException, InterruptedException {
        return ((LcMacroStateResponse) send(new LcMacroHandleMessage(macroNumber, macroOperationCode))).getMacroState();
    }

    public boolean isExternalStartEnabled() throws IOException, ProtocolException, InterruptedException {
        boolean result = false;
        Feature feature = getFeature(BidibLibrary.FEATURE_CTRL_MAC_START_MAN);

        if (feature != null) {
            result = feature.getValue() == 1;
        }
        return result;
    }

    public void setConfig(LcConfig config) throws IOException, ProtocolException, InterruptedException {
        if (config != null) {
            send(new LcConfigSetMessage(config));
        }
    }

    public LcMacro setMacro(LcMacro macro) throws IOException, ProtocolException, InterruptedException {
        return ((LcMacroResponse) send(new LcMacroSetMessage(macro))).getMacro();
    }

    public void setMacroParameter(int macroNumber, int parameter, int value) throws IOException, ProtocolException,
            InterruptedException {
        send(new LcMacroParaSetMessage(macroNumber, parameter, value));
    }

    public void setOutput(LcOutputType outputType, int outputNumber, int state) throws IOException, ProtocolException,
            InterruptedException {
        send(new LcOutputMessage(outputType, outputNumber, state));
    }
}
