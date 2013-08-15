package org.bidib.jbidibc.lcmacro;

import org.bidib.jbidibc.LcMacro;
import org.bidib.jbidibc.enumeration.AnalogPortEnum;
import org.bidib.jbidibc.enumeration.BidibEnum;
import org.bidib.jbidibc.enumeration.LcOutputType;
import org.bidib.jbidibc.enumeration.LightPortEnum;
import org.bidib.jbidibc.enumeration.ServoPortEnum;
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
}
