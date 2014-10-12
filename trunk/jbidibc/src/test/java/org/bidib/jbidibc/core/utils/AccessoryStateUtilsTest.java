package org.bidib.jbidibc.core.utils;

import org.bidib.jbidibc.core.utils.AccessoryStateUtils;
import org.bidib.jbidibc.core.utils.AccessoryStateUtils.ErrorAccessoryState;
import org.bidib.jbidibc.core.utils.AccessoryStateUtils.ErrorAccessoryState.AccessoryExecutionState;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AccessoryStateUtilsTest {

    @Test
    public void getErrorState() {
        ErrorAccessoryState state = AccessoryStateUtils.getErrorState((byte) 0x3F);
        Assert.assertEquals(state, ErrorAccessoryState.INTERNAL_ERROR);

        state = AccessoryStateUtils.getErrorState((byte) 0x00);
        Assert.assertEquals(state, ErrorAccessoryState.NO_MORE_ERROR);

        state = AccessoryStateUtils.getErrorState((byte) 0x01);
        Assert.assertEquals(state, ErrorAccessoryState.COMMAND_NOT_EXECUTABLE_UNKNOWN_COMMAND_OR_ASPECT);

        state = AccessoryStateUtils.getErrorState((byte) 0x02);
        Assert.assertEquals(state, ErrorAccessoryState.POWER_CONSUMPTION_HIGH);

        state = AccessoryStateUtils.getErrorState((byte) 0x03);
        Assert.assertEquals(state, ErrorAccessoryState.POWER_SUPPLY_BELOW_LIMITS);

        state = AccessoryStateUtils.getErrorState((byte) 0x04);
        Assert.assertEquals(state, ErrorAccessoryState.FUSE_BLOWN);

        state = AccessoryStateUtils.getErrorState((byte) 0x05);
        Assert.assertEquals(state, ErrorAccessoryState.TEMPERATURE_TOO_HIGH);

        state = AccessoryStateUtils.getErrorState((byte) 0x06);
        Assert.assertEquals(state, ErrorAccessoryState.FEEDBACK_ERROR_UNWANTED_CHANGE_POSITION);

        state = AccessoryStateUtils.getErrorState((byte) 0x07);
        Assert.assertEquals(state, ErrorAccessoryState.MANUAL_CONTROL);

        state = AccessoryStateUtils.getErrorState((byte) 0x10);
        Assert.assertEquals(state, ErrorAccessoryState.BULB_OUT_OF_ORDER);

        state = AccessoryStateUtils.getErrorState((byte) 0x20);
        Assert.assertEquals(state, ErrorAccessoryState.SERVO_OUT_OF_ORDER);
    }

    @Test
    public void hasError() {
        boolean hasError = AccessoryStateUtils.hasError((byte) 0x80);
        Assert.assertEquals(hasError, true);

        hasError = AccessoryStateUtils.hasError((byte) 0x40);
        Assert.assertEquals(hasError, false);
    }

    @Test
    public void hasMoreErrors() {
        boolean hasMoreErrors = AccessoryStateUtils.hasMoreErrors((byte) 0x4F);
        Assert.assertEquals(hasMoreErrors, true);

        hasMoreErrors = AccessoryStateUtils.hasMoreErrors((byte) 0x1F);
        Assert.assertEquals(hasMoreErrors, false);

        hasMoreErrors = AccessoryStateUtils.hasMoreErrors((byte) 0x3F);
        Assert.assertEquals(hasMoreErrors, false);
    }

    @Test
    public void getExecutionState() {

        AccessoryExecutionState executionState = AccessoryStateUtils.getExecutionState((byte) 0x00);
        Assert.assertEquals(AccessoryExecutionState.SUCCESSFUL, executionState);

        executionState = AccessoryStateUtils.getExecutionState((byte) 0x01);
        Assert.assertEquals(AccessoryExecutionState.RUNNING, executionState);

        executionState = AccessoryStateUtils.getExecutionState((byte) 0x02);
        Assert.assertEquals(AccessoryExecutionState.SUCCESSFUL, executionState);

        executionState = AccessoryStateUtils.getExecutionState((byte) 0x03);
        Assert.assertEquals(AccessoryExecutionState.RUNNING, executionState);

        executionState = AccessoryStateUtils.getExecutionState((byte) 0x80);
        Assert.assertEquals(AccessoryExecutionState.ERROR, executionState);
    }
}
