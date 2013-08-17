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
    public void prepareLcMacroStepAnalogPort() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.ANALOGPORT, (byte) 1 /*outputnumber*/, (BidibEnum) AnalogPortEnum.START, (byte) 1 /*value*/);

        LcMacroStepType step = exporter.prepareLcMacroStep(lcMacro);
        Assert.assertNotNull(step);

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(step.getAnalogPortActionType(), AnalogPortActionType.START);
        Assert.assertNull(step.getCriticalSectionActionType());
    }

    @Test
    public void prepareLcMacroStepLightPort() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.LIGHTPORT, (byte) 1 /*outputnumber*/, (BidibEnum) LightPortEnum.ON, (byte) 1 /*value*/);

        LcMacroStepType step = exporter.prepareLcMacroStep(lcMacro);
        Assert.assertNotNull(step);

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(step.getLightPortActionType(), LightPortActionType.ON);
        Assert.assertNull(step.getCriticalSectionActionType());
    }

    @Test
    public void prepareLcMacroStepMotorPortBackward() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.MOTORPORT, (byte) 1 /*outputnumber*/, (BidibEnum) MotorPortEnum.BACKWARD, (byte) 1 /*value*/);

        LcMacroStepType step = exporter.prepareLcMacroStep(lcMacro);
        Assert.assertNotNull(step);

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(step.getMotorPortActionType(), MotorPortActionType.BACKWARD);
        Assert.assertNull(step.getCriticalSectionActionType());
    }

    @Test
    public void prepareLcMacroStepMotorPortForward() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.MOTORPORT, (byte) 1 /*outputnumber*/, (BidibEnum) MotorPortEnum.FORWARD, (byte) 1 /*value*/);

        LcMacroStepType step = exporter.prepareLcMacroStep(lcMacro);
        Assert.assertNotNull(step);

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(step.getMotorPortActionType(), MotorPortActionType.FORWARD);
        Assert.assertNull(step.getCriticalSectionActionType());
    }

    @Test
    public void prepareLcMacroStepServoPort() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.SERVOPORT, (byte) 1 /*outputnumber*/, (BidibEnum) ServoPortEnum.START, (byte) 12 /*value*/);

        LcMacroStepType step = exporter.prepareLcMacroStep(lcMacro);
        Assert.assertNotNull(step);
        Assert.assertNotNull(step.getServoPortActionType());

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(step.getServoPortActionType().getAction(), ServoActionType.START);
        Assert.assertEquals(step.getServoPortActionType().getDestination(), 12);
        Assert.assertNull(step.getAnalogPortActionType());
    }

    @Test
    public void prepareLcMacroStepSoundPort() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.SOUNDPORT, (byte) 1 /*outputnumber*/, (BidibEnum) SoundPortEnum.START, (byte) 12 /*value*/);

        LcMacroStepType step = exporter.prepareLcMacroStep(lcMacro);
        Assert.assertNotNull(step);

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(step.getSoundPortActionType(), SoundPortActionType.START);
        Assert.assertNull(step.getAnalogPortActionType());
    }

    @Test
    public void prepareLcMacroStepSwitchPortOn() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.SWITCHPORT, (byte) 1 /*outputnumber*/, (BidibEnum) SwitchPortEnum.ON, (byte) 12 /*value*/);

        LcMacroStepType step = exporter.prepareLcMacroStep(lcMacro);
        Assert.assertNotNull(step);

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(step.getSwitchPortActionType(), SwitchPortActionType.ON);
        Assert.assertNull(step.getAnalogPortActionType());
    }

    @Test
    public void prepareLcMacroStepSwitchPortOff() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.SWITCHPORT, (byte) 1 /*outputnumber*/, (BidibEnum) SwitchPortEnum.OFF, (byte) 12 /*value*/);

        LcMacroStepType step = exporter.prepareLcMacroStep(lcMacro);
        Assert.assertNotNull(step);

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(step.getSwitchPortActionType(), SwitchPortActionType.OFF);
        Assert.assertNull(step.getAnalogPortActionType());
    }

    @Test
    public void prepareLcMacroStepDelay() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/, LcOutputType.DELAY,
                (byte) 100 /*outputnumber*/, (BidibEnum) null, (byte) 12 /*value*/);

        LcMacroStepType step = exporter.prepareLcMacroStep(lcMacro);
        Assert.assertNotNull(step);

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(step.getDelayActionType(), Integer.valueOf(100));
        Assert.assertNull(step.getAnalogPortActionType());
    }

    @Test
    public void prepareLcMacroStepRandomDelay() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.RANDOM_DELAY, (byte) 100 /*outputnumber*/, (BidibEnum) null, (byte) 12 /*value*/);

        LcMacroStepType step = exporter.prepareLcMacroStep(lcMacro);
        Assert.assertNotNull(step);

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(step.getRandomDelayActionType(), Integer.valueOf(100));
        Assert.assertNull(step.getAnalogPortActionType());
    }

    @Test
    public void prepareLcMacroStepInputQuery0() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.INPUT_QUERY0, (byte) 11 /*outputnumber*/, (BidibEnum) null, (byte) 12 /*value*/);

        LcMacroStepType step = exporter.prepareLcMacroStep(lcMacro);
        Assert.assertNotNull(step);

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(step.getInputQuery0ActionType(), Integer.valueOf(11));
        Assert.assertNull(step.getAnalogPortActionType());
    }

    @Test
    public void prepareLcMacroStepInputQuery1() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.INPUT_QUERY1, (byte) 13 /*outputnumber*/, (BidibEnum) null, (byte) 12 /*value*/);

        LcMacroStepType step = exporter.prepareLcMacroStep(lcMacro);
        Assert.assertNotNull(step);

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(step.getInputQuery1ActionType(), Integer.valueOf(13));
        Assert.assertNull(step.getAnalogPortActionType());
    }

    @Test
    public void prepareLcMacroStepFlagClear() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.FLAG_CLEAR, (byte) 13 /*outputnumber*/, (BidibEnum) null, (byte) 12 /*value*/);

        LcMacroStepType step = exporter.prepareLcMacroStep(lcMacro);
        Assert.assertNotNull(step);
        Assert.assertNotNull(step.getFlagActionType());
        Assert.assertNotNull(step.getFlagActionType().getOperation());

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(step.getFlagActionType().getOperation(), FlagOperationType.CLEAR);
        Assert.assertEquals(step.getFlagActionType().getPortNumber(), 13);
        Assert.assertNull(step.getAnalogPortActionType());
    }

    @Test
    public void prepareLcMacroStepFlagSet() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.FLAG_SET, (byte) 13 /*outputnumber*/, (BidibEnum) null, (byte) 12 /*value*/);

        LcMacroStepType step = exporter.prepareLcMacroStep(lcMacro);
        Assert.assertNotNull(step);
        Assert.assertNotNull(step.getFlagActionType());
        Assert.assertNotNull(step.getFlagActionType().getOperation());

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(step.getFlagActionType().getOperation(), FlagOperationType.SET);
        Assert.assertEquals(step.getFlagActionType().getPortNumber(), 13);
        Assert.assertNull(step.getAnalogPortActionType());
    }

    @Test
    public void prepareLcMacroStepFlagQuery() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.FLAG_QUERY, (byte) 13 /*outputnumber*/, (BidibEnum) null, (byte) 12 /*value*/);

        LcMacroStepType step = exporter.prepareLcMacroStep(lcMacro);
        Assert.assertNotNull(step);
        Assert.assertNotNull(step.getFlagActionType());
        Assert.assertNotNull(step.getFlagActionType().getOperation());

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(step.getFlagActionType().getOperation(), FlagOperationType.QUERY);
        Assert.assertEquals(step.getFlagActionType().getPortNumber(), 13);
        Assert.assertNull(step.getAnalogPortActionType());
    }

    @Test
    public void prepareLcMacroStepCriticalSectionBegin() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.BEGIN_CRITICAL, (byte) 0 /*outputnumber*/, (BidibEnum) null, (byte) 12 /*value*/);

        LcMacroStepType step = exporter.prepareLcMacroStep(lcMacro);
        Assert.assertNotNull(step);

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(step.getCriticalSectionActionType(), CriticalSectionActionType.BEGIN);
        Assert.assertNull(step.getAnalogPortActionType());
    }

    @Test
    public void prepareLcMacroStepCriticalSectionEnd() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.END_CRITICAL, (byte) 0 /*outputnumber*/, (BidibEnum) null, (byte) 12 /*value*/);

        LcMacroStepType step = exporter.prepareLcMacroStep(lcMacro);
        Assert.assertNotNull(step);

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(step.getCriticalSectionActionType(), CriticalSectionActionType.END);
        Assert.assertNull(step.getAnalogPortActionType());
    }

    @Test
    public void prepareLcMacroStopMacro() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.STOP_MACRO, (byte) 13 /*outputnumber*/, (BidibEnum) null, (byte) 12 /*value*/);

        LcMacroStepType step = exporter.prepareLcMacroStep(lcMacro);
        Assert.assertNotNull(step);
        Assert.assertNotNull(step.getMacroActionType());
        Assert.assertNotNull(step.getMacroActionType().getOperation());

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(step.getMacroActionType().getOperation(), MacroOperationType.STOP);
        Assert.assertEquals(step.getMacroActionType().getPortNumber(), 13);
        Assert.assertNull(step.getAnalogPortActionType());
    }

    @Test
    public void prepareLcMacroStartMacro() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.START_MACRO, (byte) 13 /*outputnumber*/, (BidibEnum) null, (byte) 12 /*value*/);

        LcMacroStepType step = exporter.prepareLcMacroStep(lcMacro);
        Assert.assertNotNull(step);
        Assert.assertNotNull(step.getMacroActionType());
        Assert.assertNotNull(step.getMacroActionType().getOperation());

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(step.getMacroActionType().getOperation(), MacroOperationType.START);
        Assert.assertEquals(step.getMacroActionType().getPortNumber(), 13);
        Assert.assertNull(step.getAnalogPortActionType());
    }

    @Test
    public void prepareLcMacroEndMacro() {
        LcMacroExporter exporter = new LcMacroExporter();

        LcMacro lcMacro =
            new LcMacro((byte) 0 /*macronumber*/, (byte) 1/*stepnumber*/, (byte) 60 /*delay*/,
                LcOutputType.END_OF_MACRO, (byte) 13 /*outputnumber*/, (BidibEnum) null, (byte) 12 /*value*/);

        LcMacroStepType step = exporter.prepareLcMacroStep(lcMacro);
        Assert.assertNotNull(step);
        Assert.assertNotNull(step.getMacroActionType());
        Assert.assertNotNull(step.getMacroActionType().getOperation());

        Assert.assertEquals(step.getDelay(), Integer.valueOf(60));
        Assert.assertEquals(step.getStepNumber(), 1);
        Assert.assertEquals(step.getMacroActionType().getOperation(), MacroOperationType.END);
        Assert.assertEquals(step.getMacroActionType().getPortNumber(), 13);
        Assert.assertNull(step.getAnalogPortActionType());
    }
}
