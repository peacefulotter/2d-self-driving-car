package com.peacefulotter.selfdrivingcar.game.car;

import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public enum CarColor
{
    NO_COLOR(72, 202, 228 ),
    SELECTED_COLOR( 239, 71, 111 ),
    PARENT_COLOR(  6, 214, 160 ),
    CROSSED_PARENT(  255, 209, 102 ),
    DEAD_COLOR( 74, 78, 105 );

    private final Color color;

    CarColor( int r, int g, int b )
    {
        color = Color.rgb(r, g, b);
    }

    public static Color interpolateColors(Color origin, Color target, double ratio) {
        var opacity = lerp(ratio, origin.getOpacity(), target.getOpacity());
        return opacity == 0.0 ? Color.TRANSPARENT : Color.color(
            boundedLerp(ratio, origin.getRed() * origin.getOpacity(), target.getRed() * target.getOpacity()) / opacity,
            boundedLerp(ratio, origin.getGreen() * origin.getOpacity(), target.getGreen() * target.getOpacity()) / opacity,
            boundedLerp(ratio, origin.getBlue() * origin.getOpacity(), target.getBlue() * target.getOpacity()) / opacity,
            opacity
        );
    }

    public static void setCarColor( ImageView img, Color color )
    {
        ColorAdjust monochrome = new ColorAdjust();
        monochrome.setSaturation(-1.0);
        ColorInput colorInput = new ColorInput(0, 0, img.getImage().getWidth(), img.getImage().getHeight(), color);
        Blend blend = new Blend( BlendMode.MULTIPLY, monochrome, colorInput );
        img.setEffect( blend );
    }

    private static double lerp(double ratio, double origin, double target) {
        return origin + (target - origin) * ratio;
    }

    private static double boundedLerp( double ratio, double origin, double target )
    {
        return Math.min(1, Math.max(lerp(ratio, origin, target), 0));
    }

    public Color getColor() { return color; }
}