package org.bidib.jbidibc.exchange.dmxscenery;

import java.awt.Color;

public class LineColorUtils {

    /**
     * @param colorType
     *            the color type
     * @return the AWT color
     */
    public static Color getColor(ColorType colorType) {
        if (colorType == null) {
            return null;
        }
        return new Color(colorType.getRed(), colorType.getGreen(), colorType.getBlue());
    }

    /**
     * @param color
     *            the AWT color
     * @return the color type
     */
    public static ColorType getColorType(Color color) {
        if (color == null) {
            return null;
        }

        return new ColorType().withRed(color.getRed()).withGreen(color.getGreen()).withBlue(color.getBlue());
    }
}
