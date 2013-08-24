package org.bidib.jbidibc.lcmacro;

import org.bidib.jbidibc.LcMacro;
import org.bidib.jbidibc.enumeration.AnalogPortEnum;
import org.bidib.jbidibc.enumeration.BidibEnum;
import org.bidib.jbidibc.enumeration.LcOutputType;
import org.bidib.jbidibc.enumeration.LightPortEnum;
import org.bidib.jbidibc.enumeration.MotorPortEnum;
import org.bidib.jbidibc.enumeration.ServoPortEnum;
import org.bidib.jbidibc.enumeration.SoundPortEnum;
import org.bidib.jbidibc.enumeration.SwitchPortEnum;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LcMacroExporterTest {

    @Test
    public void prepareLcMacroPointAnalogPort() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.ANALOGPORT, (byte) 1 /*outputnumber*/, (BidibEnum) AnalogPortEnum.START, (byte) 1 /*value*/);

        LcMacroPointType step = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(step);
        Assert.assertTrue(step instanceof AnalogPortStep);
        AnalogPortStep analogPortStep = (AnalogPortStep) step;

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);

        Assert.assertEquals(analogPortStep.getAnalogPortActionType(), AnalogPortActionType.START);
    }

    @Test
    public void prepareLcMacroPointLightPort() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.LIGHTPORT, (byte) 1 /*outputnumber*/, (BidibEnum) LightPortEnum.ON, (byte) 1 /*value*/);

        LcMacroPointType step = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(step);
        Assert.assertTrue(step instanceof LightPortStep);
        LightPortStep lightPortStep = (LightPortStep) step;

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(lightPortStep.getLightPortActionType(), LightPortActionType.ON);
    }

    @Test
    public void prepareLcMacroPointMotorPortBackward() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.MOTORPORT, (byte) 1 /*outputnumber*/, (BidibEnum) MotorPortEnum.BACKWARD, (byte) 1 /*value*/);

        LcMacroPointType step = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(step);
        Assert.assertTrue(step instanceof MotorPortStep);
        MotorPortStep motorPortStep = (MotorPortStep) step;

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(motorPortStep.getMotorPortActionType(), MotorPortActionType.BACKWARD);
    }

    @Test
    public void prepareLcMacroPointMotorPortForward() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.MOTORPORT, (byte) 1 /*outputnumber*/, (BidibEnum) MotorPortEnum.FORWARD, (byte) 1 /*value*/);

        LcMacroPointType step = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(step);
        Assert.assertTrue(step instanceof MotorPortStep);
        MotorPortStep motorPortStep = (MotorPortStep) step;

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(motorPortStep.getMotorPortActionType(), MotorPortActionType.FORWARD);
    }

    @Test
    public void prepareLcMacroPointServoPort() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.SERVOPORT, (byte) 1 /*outputnumber*/, (BidibEnum) ServoPortEnum.START, (byte) 12 /*value*/);

        LcMacroPointType step = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(step);
        Assert.assertTrue(step instanceof ServoPortStep);
        ServoPortStep servoPortStep = (ServoPortStep) step;

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);

        Assert.assertNotNull(servoPortStep.getServoPortActionType());
        Assert.assertEquals(servoPortStep.getServoPortActionType().getAction(), ServoActionType.START);
        Assert.assertEquals(servoPortStep.getServoPortActionType().getDestination(), 12);
    }

    @Test
    public void prepareLcMacroPointSoundPort() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.SOUNDPORT, (byte) 1 /*outputnumber*/, (BidibEnum) SoundPortEnum.START, (byte) 12 /*value*/);

        LcMacroPointType step = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(step);
        Assert.assertTrue(step instanceof SoundPortStep);
        SoundPortStep soundPortStep = (SoundPortStep) step;

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(soundPortStep.getSoundPortActionType(), SoundPortActionType.START);
    }

    @Test
    public void prepareLcMacroPointSwitchPortOn() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.SWITCHPORT, (byte) 1 /*outputnumber*/, (BidibEnum) SwitchPortEnum.ON, (byte) 12 /*value*/);

        LcMacroPointType step = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(step);
        Assert.assertTrue(step instanceof SwitchPortStep);
        SwitchPortStep switchPortStep = (SwitchPortStep) step;

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(switchPortStep.getSwitchPortActionType(), SwitchPortActionType.ON);
    }

    @Test
    public void prepareLcMacroPointSwitchPortOff() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.SWITCHPORT, (byte) 1 /*outputnumber*/, (BidibEnum) SwitchPortEnum.OFF, (byte) 12 /*value*/);

        LcMacroPointType step = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(step);
        Assert.assertTrue(step instanceof SwitchPortStep);
        SwitchPortStep switchPortStep = (SwitchPortStep) step;

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(switchPortStep.getSwitchPortActionType(), SwitchPortActionType.OFF);
    }

    @Test
    public void prepareLcMacroPointDelay() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/, LcOutputType.DELAY,
                (byte) 100 /*outputnumber*/, (BidibEnum) null, (byte) 12 /*value*/);

        LcMacroPointType step = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(step);
        Assert.assertTrue(step instanceof DelayStep);
        DelayStep delayStep = (DelayStep) step;

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(delayStep.getDelayActionType(), 100);
    }

    @Test
    public void prepareLcMacroPointRandomDelay() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.RANDOM_DELAY, (byte) 100 /*outputnumber*/, (BidibEnum) null, (byte) 12 /*value*/);

        LcMacroPointType step = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(step);
        Assert.assertTrue(step instanceof RandomDelayStep);
        RandomDelayStep randomDelayStep = (RandomDelayStep) step;

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(randomDelayStep.getRandomDelayActionType(), 100);
    }

    @Test
    public void prepareLcMacroPointInputQuery0() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.INPUT_QUERY0, (byte) 11 /*outputnumber*/, (BidibEnum) null, (byte) 12 /*value*/);

        LcMacroPointType step = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(step);
        Assert.assertTrue(step instanceof InputQuery0Step);
        InputQuery0Step inputQuery0Step = (InputQuery0Step) step;

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(inputQuery0Step.getInputQuery0ActionType(), 11);
    }

    @Test
    public void prepareLcMacroPointInputQuery1() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.INPUT_QUERY1, (byte) 13 /*outputnumber*/, (BidibEnum) null, (byte) 12 /*value*/);

        LcMacroPointType step = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(step);
        Assert.assertTrue(step instanceof InputQuery1Step);
        InputQuery1Step inputQuery1Step = (InputQuery1Step) step;

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(inputQuery1Step.getInputQuery1ActionType(), 13);
    }

    @Test
    public void prepareLcMacroPointFlagClear() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.FLAG_CLEAR, (byte) 13 /*outputnumber*/, (BidibEnum) null, (byte) 12 /*value*/);

        LcMacroPointType step = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(step);
        Assert.assertTrue(step instanceof FlagStep);
        FlagStep flagStep = (FlagStep) step;

        Assert.assertNotNull(flagStep.getFlagActionType());
        Assert.assertNotNull(flagStep.getFlagActionType().getOperation());

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(flagStep.getFlagActionType().getOperation(), FlagOperationType.CLEAR);
        Assert.assertEquals(flagStep.getFlagActionType().getPortNumber(), 13);
    }

    @Test
    public void prepareLcMacroPointFlagSet() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.FLAG_SET, (byte) 13 /*outputnumber*/, (BidibEnum) null, (byte) 12 /*value*/);

        LcMacroPointType step = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(step);
        Assert.assertTrue(step instanceof FlagStep);
        FlagStep flagStep = (FlagStep) step;

        Assert.assertNotNull(flagStep.getFlagActionType());
        Assert.assertNotNull(flagStep.getFlagActionType().getOperation());

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(flagStep.getFlagActionType().getOperation(), FlagOperationType.SET);
        Assert.assertEquals(flagStep.getFlagActionType().getPortNumber(), 13);
    }

    @Test
    public void prepareLcMacroPointFlagQuery() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.FLAG_QUERY, (byte) 13 /*outputnumber*/, (BidibEnum) null, (byte) 12 /*value*/);

        LcMacroPointType step = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(step);
        Assert.assertTrue(step instanceof FlagStep);
        FlagStep flagStep = (FlagStep) step;

        Assert.assertNotNull(flagStep.getFlagActionType());
        Assert.assertNotNull(flagStep.getFlagActionType().getOperation());

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(flagStep.getFlagActionType().getOperation(), FlagOperationType.QUERY);
        Assert.assertEquals(flagStep.getFlagActionType().getPortNumber(), 13);
    }

    @Test
    public void prepareLcMacroPointCriticalSectionBegin() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.BEGIN_CRITICAL, (byte) 0 /*outputnumber*/, (BidibEnum) null, (byte) 12 /*value*/);

        LcMacroPointType step = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(step);
        Assert.assertTrue(step instanceof CriticalSectionStep);
        CriticalSectionStep criticalSectionStep = (CriticalSectionStep) step;

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(criticalSectionStep.getCriticalSectionActionType(), CriticalSectionActionType.BEGIN);
    }

    @Test
    public void prepareLcMacroPointCriticalSectionEnd() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.END_CRITICAL, (byte) 0 /*outputnumber*/, (BidibEnum) null, (byte) 12 /*value*/);

        LcMacroPointType step = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(step);
        Assert.assertTrue(step instanceof CriticalSectionStep);
        CriticalSectionStep criticalSectionStep = (CriticalSectionStep) step;

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(criticalSectionStep.getCriticalSectionActionType(), CriticalSectionActionType.END);
    }

    @Test
    public void prepareLcMacroStopMacro() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.STOP_MACRO, (byte) 13 /*outputnumber*/, (BidibEnum) null, (byte) 12 /*value*/);

        LcMacroPointType step = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(step);
        Assert.assertTrue(step instanceof MacroActionStep);
        MacroActionStep macroActionStep = (MacroActionStep) step;

        Assert.assertNotNull(macroActionStep.getMacroActionType());
        Assert.assertNotNull(macroActionStep.getMacroActionType().getOperation());

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(macroActionStep.getMacroActionType().getOperation(), MacroOperationType.STOP);
        Assert.assertEquals(macroActionStep.getMacroActionType().getPortNumber(), 13);
    }

    @Test
    public void prepareLcMacroStartMacro() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.START_MACRO, (byte) 13 /*outputnumber*/, (BidibEnum) null, (byte) 12 /*value*/);

        LcMacroPointType step = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(step);
        Assert.assertTrue(step instanceof MacroActionStep);
        MacroActionStep macroActionStep = (MacroActionStep) step;

        Assert.assertNotNull(macroActionStep.getMacroActionType());
        Assert.assertNotNull(macroActionStep.getMacroActionType().getOperation());

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(macroActionStep.getMacroActionType().getOperation(), MacroOperationType.START);
        Assert.assertEquals(macroActionStep.getMacroActionType().getPortNumber(), 13);
    }

    @Test
    public void prepareLcMacroEndMacro() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.END_OF_MACRO, (byte) 13 /*outputnumber*/, (BidibEnum) null, (byte) 12 /*value*/);

        LcMacroPointType step = exporter.prepareLcMacroPoint(lcMacro);
        Assert.assertNotNull(step);
        Assert.assertTrue(step instanceof MacroActionStep);
        MacroActionStep macroActionStep = (MacroActionStep) step;

        Assert.assertNotNull(macroActionStep.getMacroActionType());
        Assert.assertNotNull(macroActionStep.getMacroActionType().getOperation());

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(macroActionStep.getMacroActionType().getOperation(), MacroOperationType.END);
        Assert.assertEquals(macroActionStep.getMacroActionType().getPortNumber(), 13);
    }
}
