package com.peacefulotter.selfdrivingcar.game.car;

import javafx.scene.effect.ColorAdjust;

public enum CarColor
{
    NO_COLOR(0, 0, 0, 0),
    SELECTED_COLOR( 0.6, 1, 0.5, 1 ),
    PARENT_COLOR( -0.6, 0.7, 0.4, 1 ),
    CROSSED_PARENT( 0.3, 1, 0.8, 1 ),
    DEAD_COLOR( 0.95, 0.2, 0.1, 0.4 );

    private final ColorAdjust color;

    CarColor( double hue, double saturation, double brightness, double contrast)
    {
        color = new ColorAdjust(hue, saturation, brightness, contrast);
    }

    public ColorAdjust getColor() { return color; }
}