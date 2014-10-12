package org.bidib.jbidibc.lcmacro;

import org.bidib.jbidibc.core.LcMacro;
import org.bidib.jbidibc.core.enumeration.AnalogPortEnum;
import org.bidib.jbidibc.core.enumeration.BidibEnum;
import org.bidib.jbidibc.core.enumeration.LcOutputType;
import org.bidib.jbidibc.core.enumeration.LightPortEnum;
import org.bidib.jbidibc.core.enumeration.MotorPortEnum;
import org.bidib.jbidibc.core.enumeration.ServoPortEnum;
import org.bidib.jbidibc.core.enumeration.SoundPortEnum;
import org.bidib.jbidibc.core.enumeration.SwitchPortEnum;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LcMacroExporterTest {

    @Test
    public void prepareLcMacroPointAnalogPort() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /* macronumber */, (byte) 1/* stepnumber */, (byte) 60 /* delay */,
                LcOutputType.ANALOGPORT, (byte) 1 /* outputnumber */, (BidibEnum) AnalogPortEnum.START, (byte) 1 /* value */);

        LcMacroPointType point = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(point);
        Assert.assertTrue(point instanceof AnalogPortPoint);
        AnalogPortPoint analogPortPoint = (AnalogPortPoint) point;

        // Assert.assertEquals(point.getIndex(), 1);

        Assert.assertEquals(analogPortPoint.getDelay(), Integer.valueOf(60));
        Assert.assertNotNull(analogPortPoint.getAnalogPortActionType());
        Assert.assertEquals(analogPortPoint.getAnalogPortActionType().getAction(), AnalogActionType.START);
        Assert.assertEquals(analogPortPoint.getAnalogPortActionType().getValue(), 1);
        Assert.assertEquals(analogPortPoint.getOutputNumber(), 1);
    }

    @Test
    public void prepareLcMacroPointLightPort() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /* macronumber */, (byte) 1/* stepnumber */, (byte) 60 /* delay */,
                LcOutputType.LIGHTPORT, (byte) 1 /* outputnumber */, (BidibEnum) LightPortEnum.ON, (byte) 1 /* value */);

        LcMacroPointType point = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(point);
        Assert.assertTrue(point instanceof LightPortPoint);
        LightPortPoint lightPortPoint = (LightPortPoint) point;

        // Assert.assertEquals(point.getIndex(), 1);
        Assert.assertEquals(lightPortPoint.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(lightPortPoint.getLightPortActionType(), LightPortActionType.ON);
        Assert.assertEquals(lightPortPoint.getOutputNumber(), 1);
    }

    @Test
    public void prepareLcMacroPointMotorPortBackward() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /* macronumber */, (byte) 1/* stepnumber */, (byte) 60 /* delay */,
                LcOutputType.MOTORPORT, (byte) 1 /* outputnumber */, (BidibEnum) MotorPortEnum.BACKWARD, (byte) 123 /* value */);

        LcMacroPointType point = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(point);
        Assert.assertTrue(point instanceof MotorPortPoint);
        MotorPortPoint motorPortPoint = (MotorPortPoint) point;

        // Assert.assertEquals(point.getIndex(), 1);
        Assert.assertEquals(motorPortPoint.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(motorPortPoint.getMotorPortActionType().getAction(), MotorActionType.BACKWARD);
        Assert.assertEquals(motorPortPoint.getMotorPortActionType().getValue(), 123);
        Assert.assertEquals(motorPortPoint.getOutputNumber(), 1);
    }

    @Test
    public void prepareLcMacroPointMotorPortForward() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /* macronumber */, (byte) 1/* stepnumber */, (byte) 60 /* delay */,
                LcOutputType.MOTORPORT, (byte) 1 /* outputnumber */, (BidibEnum) MotorPortEnum.FORWARD, (byte) 123 /* value */);

        LcMacroPointType point = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(point);
        Assert.assertTrue(point instanceof MotorPortPoint);
        MotorPortPoint motorPortPoint = (MotorPortPoint) point;

        // Assert.assertEquals(point.getIndex(), 1);
        Assert.assertEquals(motorPortPoint.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(motorPortPoint.getMotorPortActionType().getAction(), MotorActionType.FORWARD);
        Assert.assertEquals(motorPortPoint.getMotorPortActionType().getValue(), 123);
        Assert.assertEquals(motorPortPoint.getOutputNumber(), 1);
    }

    @Test
    public void prepareLcMacroPointServoPort() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /* macronumber */, (byte) 1/* stepnumber */, (byte) 60 /* delay */,
                LcOutputType.SERVOPORT, (byte) 1 /* outputnumber */, (BidibEnum) ServoPortEnum.START, (byte) 12 /* value */);

        LcMacroPointType point = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(point);
        Assert.assertTrue(point instanceof ServoPortPoint);
        ServoPortPoint servoPortPoint = (ServoPortPoint) point;

        // Assert.assertEquals(point.getIndex(), 1);

        Assert.assertEquals(servoPortPoint.getDelay(), Integer.valueOf(60));
        Assert.assertNotNull(servoPortPoint.getServoPortActionType());
        Assert.assertEquals(servoPortPoint.getServoPortActionType().getAction(), ServoActionType.START);
        Assert.assertEquals(servoPortPoint.getServoPortActionType().getDestination(), 12);
        Assert.assertEquals(servoPortPoint.getOutputNumber(), 1);
    }

    @Test
    public void prepareLcMacroPointSoundPort() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /* macronumber */, (byte) 1/* stepnumber */, (byte) 60 /* delay */,
                LcOutputType.SOUNDPORT, (byte) 1 /* outputnumber */, (BidibEnum) SoundPortEnum.PLAY, (byte) 12 /* value */);

        LcMacroPointType point = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(point);
        Assert.assertTrue(point instanceof SoundPortPoint);
        SoundPortPoint soundPortPoint = (SoundPortPoint) point;

        // Assert.assertEquals(point.getIndex(), 1);
        Assert.assertEquals(soundPortPoint.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(soundPortPoint.getSoundPortActionType().getAction(), SoundActionType.PLAY);
        Assert.assertEquals(soundPortPoint.getSoundPortActionType().getValue(), 12);
        Assert.assertEquals(soundPortPoint.getOutputNumber(), 1);
    }

    @Test
    public void prepareLcMacroPointSwitchPortOn() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /* macronumber */, (byte) 1/* stepnumber */, (byte) 60 /* delay */,
                LcOutputType.SWITCHPORT, (byte) 1 /* outputnumber */, (BidibEnum) SwitchPortEnum.ON, (byte) 12 /* value */);

        LcMacroPointType point = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(point);
        Assert.assertTrue(point instanceof SwitchPortPoint);
        SwitchPortPoint switchPortPoint = (SwitchPortPoint) point;

        // Assert.assertEquals(point.getIndex(), 1);
        Assert.assertEquals(switchPortPoint.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(switchPortPoint.getSwitchPortActionType(), SwitchPortActionType.ON);
        Assert.assertEquals(switchPortPoint.getOutputNumber(), 1);
    }

    @Test
    public void prepareLcMacroPointSwitchPortOff() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /* macronumber */, (byte) 1/* stepnumber */, (byte) 60 /* delay */,
                LcOutputType.SWITCHPORT, (byte) 1 /* outputnumber */, (BidibEnum) SwitchPortEnum.OFF, (byte) 12 /* value */);

        LcMacroPointType point = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(point);
        Assert.assertTrue(point instanceof SwitchPortPoint);
        SwitchPortPoint switchPortPoint = (SwitchPortPoint) point;

        // Assert.assertEquals(point.getIndex(), 1);
        Assert.assertEquals(switchPortPoint.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(switchPortPoint.getSwitchPortActionType(), SwitchPortActionType.OFF);
        Assert.assertEquals(switchPortPoint.getOutputNumber(), 1);
    }

    @Test
    public void prepareLcMacroPointDelay() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /* macronumber */, (byte) 1/* stepnumber */, (byte) 255 /* delay */,
                LcOutputType.DELAY, (byte) 100 /* outputnumber */, (BidibEnum) null, (byte) 12 /* value */);

        LcMacroPointType point = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(point);
        Assert.assertTrue(point instanceof DelayPoint);
        DelayPoint delayPoint = (DelayPoint) point;

        // Assert.assertEquals(point.getIndex(), 1);
        Assert.assertEquals(delayPoint.getDelayActionType(), 100);
    }

    @Test
    public void prepareLcMacroPointRandomDelay() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /* macronumber */, (byte) 1/* stepnumber */, (byte) 255 /* delay */,
                LcOutputType.RANDOM_DELAY, (byte) 100 /* outputnumber */, (BidibEnum) null, (byte) 12 /* value */);

        LcMacroPointType point = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(point);
        Assert.assertTrue(point instanceof RandomDelayPoint);
        RandomDelayPoint randomDelayPoint = (RandomDelayPoint) point;

        // Assert.assertEquals(point.getIndex(), 1);
        Assert.assertEquals(randomDelayPoint.getRandomDelayActionType(), 100);
    }

    @Test
    public void prepareLcMacroPointInputQuery0() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /* macronumber */, (byte) 1/* stepnumber */, (byte) 255 /* delay */,
                LcOutputType.INPUT_QUERY0, (byte) 11 /* outputnumber */, (BidibEnum) null, (byte) 12 /* value */);

        LcMacroPointType point = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(point);
        Assert.assertTrue(point instanceof InputPortPoint);
        InputPortPoint inputPortPoint = (InputPortPoint) point;

        // Assert.assertEquals(point.getIndex(), 1);
        Assert.assertEquals(inputPortPoint.getInputPortActionType(), InputPortActionType.QUERY_0);
        Assert.assertEquals(inputPortPoint.getInputNumber(), 11);
    }

    @Test
    public void prepareLcMacroPointInputQuery1() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /* macronumber */, (byte) 1/* stepnumber */, (byte) 255 /* delay */,
                LcOutputType.INPUT_QUERY1, (byte) 11 /* outputnumber */, (BidibEnum) null, (byte) 12 /* value */);

        LcMacroPointType point = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(point);
        Assert.assertTrue(point instanceof InputPortPoint);
        InputPortPoint inputPortPoint = (InputPortPoint) point;

        Assert.assertEquals(inputPortPoint.getInputPortActionType(), InputPortActionType.QUERY_1);
        Assert.assertEquals(inputPortPoint.getInputNumber(), 11);
    }

    @Test
    public void prepareLcMacroPointFlagClear() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /* macronumber */, (byte) 1/* stepnumber */, (byte) 255 /* delay */,
                LcOutputType.FLAG_CLEAR, (byte) 13 /* outputnumber */, (BidibEnum) null, (byte) 12 /* value */);

        LcMacroPointType point = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(point);
        Assert.assertTrue(point instanceof FlagPoint);
        FlagPoint flagPoint = (FlagPoint) point;

        Assert.assertNotNull(flagPoint.getFlagActionType());
        Assert.assertNotNull(flagPoint.getFlagActionType().getOperation());

        Assert.assertEquals(flagPoint.getFlagActionType().getOperation(), FlagOperationType.CLEAR);
        Assert.assertEquals(flagPoint.getFlagActionType().getFlagNumber(), 13);
    }

    @Test
    public void prepareLcMacroPointFlagSet() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /* macronumber */, (byte) 1/* stepnumber */, (byte) 255 /* delay */,
                LcOutputType.FLAG_SET, (byte) 13 /* outputnumber */, (BidibEnum) null, (byte) 12 /* value */);

        LcMacroPointType point = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(point);
        Assert.assertTrue(point instanceof FlagPoint);
        FlagPoint flagPoint = (FlagPoint) point;

        Assert.assertNotNull(flagPoint.getFlagActionType());
        Assert.assertNotNull(flagPoint.getFlagActionType().getOperation());

        // Assert.assertEquals(point.getIndex(), 1);
        Assert.assertEquals(flagPoint.getFlagActionType().getOperation(), FlagOperationType.SET);
        Assert.assertEquals(flagPoint.getFlagActionType().getFlagNumber(), 13);
    }

    @Test
    public void prepareLcMacroPointFlagQuery() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /* macronumber */, (byte) 1/* stepnumber */, (byte) 255 /* delay */,
                LcOutputType.FLAG_QUERY, (byte) 13 /* outputnumber */, (BidibEnum) null, (byte) 12 /* value */);

        LcMacroPointType point = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(point);
        Assert.assertTrue(point instanceof FlagPoint);
        FlagPoint flagPoint = (FlagPoint) point;

        Assert.assertNotNull(flagPoint.getFlagActionType());
        Assert.assertNotNull(flagPoint.getFlagActionType().getOperation());

        // Assert.assertEquals(point.getIndex(), 1);
        Assert.assertEquals(flagPoint.getFlagActionType().getOperation(), FlagOperationType.QUERY);
        Assert.assertEquals(flagPoint.getFlagActionType().getFlagNumber(), 13);
    }

    @Test
    public void prepareLcMacroPointCriticalSectionBegin() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /* macronumber */, (byte) 1/* stepnumber */, (byte) 255 /* delay */,
                LcOutputType.BEGIN_CRITICAL, (byte) 0 /* outputnumber */, (BidibEnum) null, (byte) 12 /* value */);

        LcMacroPointType point = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(point);
        Assert.assertTrue(point instanceof CriticalSectionPoint);
        CriticalSectionPoint criticalSectionPoint = (CriticalSectionPoint) point;

        // Assert.assertEquals(point.getIndex(), 1);
        Assert.assertEquals(criticalSectionPoint.getCriticalSectionActionType(), CriticalSectionActionType.BEGIN);
    }

    @Test
    public void prepareLcMacroPointCriticalSectionEnd() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /* macronumber */, (byte) 1/* stepnumber */, (byte) 255 /* delay */,
                LcOutputType.END_CRITICAL, (byte) 0 /* outputnumber */, (BidibEnum) null, (byte) 12 /* value */);

        LcMacroPointType point = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(point);
        Assert.assertTrue(point instanceof CriticalSectionPoint);
        CriticalSectionPoint criticalSectionPoint = (CriticalSectionPoint) point;

        // Assert.assertEquals(point.getIndex(), 1);
        Assert.assertEquals(criticalSectionPoint.getCriticalSectionActionType(), CriticalSectionActionType.END);
    }

    @Test
    public void prepareLcMacroStopMacro() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /* macronumber */, (byte) 1/* stepnumber */, (byte) 255 /* delay */,
                LcOutputType.STOP_MACRO, (byte) 13 /* outputnumber */, (BidibEnum) null, (byte) 12 /* value */);

        LcMacroPointType point = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(point);
        Assert.assertTrue(point instanceof MacroActionPoint);
        MacroActionPoint macroActionPoint = (MacroActionPoint) point;

        Assert.assertNotNull(macroActionPoint.getMacroActionType());
        Assert.assertNotNull(macroActionPoint.getMacroActionType().getOperation());

        // Assert.assertEquals(point.getIndex(), 1);
        Assert.assertEquals(macroActionPoint.getMacroActionType().getOperation(), MacroOperationType.STOP);
        Assert.assertEquals(macroActionPoint.getMacroActionType().getMacroNumber(), 13);
    }

    @Test
    public void prepareLcMacroStartMacro() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /* macronumber */, (byte) 1/* stepnumber */, (byte) 255 /* delay */,
                LcOutputType.START_MACRO, (byte) 13 /* outputnumber */, (BidibEnum) null, (byte) 12 /* value */);

        LcMacroPointType point = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(point);
        Assert.assertTrue(point instanceof MacroActionPoint);
        MacroActionPoint macroActionPoint = (MacroActionPoint) point;

        Assert.assertNotNull(macroActionPoint.getMacroActionType());
        Assert.assertNotNull(macroActionPoint.getMacroActionType().getOperation());

        // Assert.assertEquals(point.getIndex(), 1);
        Assert.assertEquals(macroActionPoint.getMacroActionType().getOperation(), MacroOperationType.START);
        Assert.assertEquals(macroActionPoint.getMacroActionType().getMacroNumber(), 13);
    }

    @Test
    public void prepareLcMacroEndMacro() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /* macronumber */, (byte) 1/* stepnumber */, (byte) 255 /* delay */,
                LcOutputType.END_OF_MACRO, (byte) 13 /* outputnumber */, (BidibEnum) null, (byte) 12 /* value */);

        LcMacroPointType point = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(point);
        Assert.assertTrue(point instanceof MacroActionPoint);
        MacroActionPoint macroActionPoint = (MacroActionPoint) point;

        Assert.assertNotNull(macroActionPoint.getMacroActionType());
        Assert.assertNotNull(macroActionPoint.getMacroActionType().getOperation());

        // Assert.assertEquals(point.getIndex(), 1);
        Assert.assertEquals(macroActionPoint.getMacroActionType().getOperation(), MacroOperationType.END);
        Assert.assertEquals(macroActionPoint.getMacroActionType().getMacroNumber(), 13);
    }
}
