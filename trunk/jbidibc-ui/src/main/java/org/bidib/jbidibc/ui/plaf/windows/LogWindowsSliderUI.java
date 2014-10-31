package org.bidib.jbidibc.ui.plaf.windows;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.plaf.ComponentUI;

import com.sun.java.swing.plaf.windows.WindowsSliderUI;

public class LogWindowsSliderUI extends WindowsSliderUI {

    public LogWindowsSliderUI(JSlider b) {
        super(b);
    }

    public static ComponentUI createUI(JComponent b) {
        return new LogWindowsSliderUI((JSlider) b);
    }

    public int xPositionForValue(int value) {
        int min = slider.getMinimum();
        int max = slider.getMaximum();
        int trackLength = trackRect.width;
        double valueRange = (double) Math.log(max) - (double) Math.log(min);
        double pixelsPerValue = (double) trackLength / valueRange;
        int trackLeft = trackRect.x;
        int trackRight = trackRect.x + (trackRect.width - 1);
        int xPosition;

        if (!drawInverted()) {
            xPosition = trackLeft;
            xPosition += Math.round(pixelsPerValue * ((double) Math.log(value) - Math.log(min)));
        }
        else {
            xPosition = trackRight;
            xPosition -= Math.round(pixelsPerValue * ((double) Math.log(value) - Math.log(min)));
        }

        xPosition = Math.max(trackLeft, xPosition);
        xPosition = Math.min(trackRight, xPosition);

        return xPosition;

    }

    public int valueForXPosition(int xPos) {
        int value;
        final int minValue = slider.getMinimum();
        final int maxValue = slider.getMaximum();
        final int trackLength = trackRect.width;
        final int trackLeft = trackRect.x;
        final int trackRight = trackRect.x + (trackRect.width - 1);

        if (xPos <= trackLeft) {
            value = drawInverted() ? maxValue : minValue;
        }
        else if (xPos >= trackRight) {
            value = drawInverted() ? minValue : maxValue;
        }
        else {
            int distanceFromTrackLeft = xPos - trackLeft;
            double valueRange = (double) Math.log(maxValue) - (double) Math.log(minValue);
            // double valuePerPixel = (double)valueRange / (double)trackLength;
            // int valueFromTrackLeft =
            // (int)Math.round( Math.pow(3.5,(double)distanceFromTrackLeft * (double)valuePerPixel));

            int valueFromTrackLeft =
                (int) Math.round(Math.pow(Math.E, Math.log(minValue)
                    + ((((double) distanceFromTrackLeft) * valueRange) / (double) trackLength)));

            value = drawInverted() ? maxValue - valueFromTrackLeft : (int) Math.log(minValue) + valueFromTrackLeft;
        }

        return value;

    }
}
