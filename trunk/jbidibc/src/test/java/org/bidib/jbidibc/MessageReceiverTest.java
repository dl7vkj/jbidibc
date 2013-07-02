package org.bidib.jbidibc;

import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

import org.bidib.jbidibc.message.BidibMessage;
import org.bidib.jbidibc.node.BidibNode;
import org.bidib.jbidibc.node.NodeFactory;
import org.mockito.Mockito;
import org.testng.annotations.Test;

public class MessageReceiverTest {

    @Test
    public void receiveMessageTest() throws IOException {

        NodeFactory nodeFactory = Mockito.mock(NodeFactory.class);
        SerialPort serialPort = Mockito.mock(SerialPort.class);
        BidibNode bidibNode = Mockito.mock(BidibNode.class);

        MessageReceiver receiver = new MessageReceiver(nodeFactory);

        ByteArrayInputStream is =
            new ByteArrayInputStream(new byte[] { 0x05, 0x00, 0x01, (byte) 0x86, (byte) 0x02, (byte) 0x00, (byte) 0x46,
                (byte) 0xFE });

        Mockito.when(serialPort.getInputStream()).thenReturn(is);
        Mockito.when(nodeFactory.getNode(Mockito.any(Node.class))).thenReturn(bidibNode);
        Mockito.when(bidibNode.getNextReceiveMsgNum(Mockito.any(BidibMessage.class))).thenReturn(Integer.valueOf(1));

        receiver.receive(serialPort);

    }

    private static final class MockSerialPort extends SerialPort {

        @Override
        public void setSerialPortParams(int b, int d, int s, int p) throws UnsupportedCommOperationException {
            // TODO Auto-generated method stub

        }

        @Override
        public int getBaudRate() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public int getDataBits() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public int getStopBits() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public int getParity() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public void setFlowControlMode(int flowcontrol) throws UnsupportedCommOperationException {
            // TODO Auto-generated method stub

        }

        @Override
        public int getFlowControlMode() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean isDTR() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void setDTR(boolean state) {
            // TODO Auto-generated method stub

        }

        @Override
        public void setRTS(boolean state) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean isCTS() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isDSR() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isCD() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isRI() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isRTS() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void sendBreak(int duration) {
            // TODO Auto-generated method stub

        }

        @Override
        public void addEventListener(SerialPortEventListener lsnr) throws TooManyListenersException {
            // TODO Auto-generated method stub

        }

        @Override
        public void removeEventListener() {
            // TODO Auto-generated method stub

        }

        @Override
        public void notifyOnDataAvailable(boolean enable) {
            // TODO Auto-generated method stub

        }

        @Override
        public void notifyOnOutputEmpty(boolean enable) {
            // TODO Auto-generated method stub

        }

        @Override
        public void notifyOnCTS(boolean enable) {
            // TODO Auto-generated method stub

        }

        @Override
        public void notifyOnDSR(boolean enable) {
            // TODO Auto-generated method stub

        }

        @Override
        public void notifyOnRingIndicator(boolean enable) {
            // TODO Auto-generated method stub

        }

        @Override
        public void notifyOnCarrierDetect(boolean enable) {
            // TODO Auto-generated method stub

        }

        @Override
        public void notifyOnOverrunError(boolean enable) {
            // TODO Auto-generated method stub

        }

        @Override
        public void notifyOnParityError(boolean enable) {
            // TODO Auto-generated method stub

        }

        @Override
        public void notifyOnFramingError(boolean enable) {
            // TODO Auto-generated method stub

        }

        @Override
        public void notifyOnBreakInterrupt(boolean enable) {
            // TODO Auto-generated method stub

        }

        @Override
        public byte getParityErrorChar() throws UnsupportedCommOperationException {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean setParityErrorChar(byte b) throws UnsupportedCommOperationException {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public byte getEndOfInputChar() throws UnsupportedCommOperationException {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean setEndOfInputChar(byte b) throws UnsupportedCommOperationException {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean setUARTType(String type, boolean test) throws UnsupportedCommOperationException {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public String getUARTType() throws UnsupportedCommOperationException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean setBaudBase(int BaudBase) throws UnsupportedCommOperationException, IOException {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public int getBaudBase() throws UnsupportedCommOperationException, IOException {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean setDivisor(int Divisor) throws UnsupportedCommOperationException, IOException {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public int getDivisor() throws UnsupportedCommOperationException, IOException {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean setLowLatency() throws UnsupportedCommOperationException {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean getLowLatency() throws UnsupportedCommOperationException {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean setCallOutHangup(boolean NoHup) throws UnsupportedCommOperationException {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean getCallOutHangup() throws UnsupportedCommOperationException {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void enableReceiveFraming(int f) throws UnsupportedCommOperationException {
            // TODO Auto-generated method stub

        }

        @Override
        public void disableReceiveFraming() {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean isReceiveFramingEnabled() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public int getReceiveFramingByte() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public void disableReceiveTimeout() {
            // TODO Auto-generated method stub

        }

        @Override
        public void enableReceiveTimeout(int time) throws UnsupportedCommOperationException {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean isReceiveTimeoutEnabled() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public int getReceiveTimeout() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public void enableReceiveThreshold(int thresh) throws UnsupportedCommOperationException {
            // TODO Auto-generated method stub

        }

        @Override
        public void disableReceiveThreshold() {
            // TODO Auto-generated method stub

        }

        @Override
        public int getReceiveThreshold() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean isReceiveThresholdEnabled() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void setInputBufferSize(int size) {
            // TODO Auto-generated method stub

        }

        @Override
        public int getInputBufferSize() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public void setOutputBufferSize(int size) {
            // TODO Auto-generated method stub

        }

        @Override
        public int getOutputBufferSize() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            // TODO Auto-generated method stub
            return null;
        }

    }
}
