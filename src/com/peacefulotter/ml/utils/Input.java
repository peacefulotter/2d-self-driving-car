package com.peacefulotter.ml.utils;

import com.peacefulotter.ml.game.Car;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public abstract class Input
{
    public static void handleKeyPressed( Car car, KeyEvent keyEvent )
    {
        KeyCode key = keyEvent.getCode();
        keyEvent.consume(); // stops the event propagation.

        switch ( key )
        {
            case UP:
                car.accelerate( 1 );
                break;
            case RIGHT:
                car.turn( 1 );
                break;
            case DOWN:
                car.accelerate( -1 );
                break;
            case LEFT:
                car.turn( -1 );
                break;
            default:
                break;
        }
    }

    public static void handleKeyReleased( Car car, KeyEvent keyEvent )
    {
        KeyCode key = keyEvent.getCode();
        keyEvent.consume(); // stops the event propagation.

        switch ( key )
        {
            case UP:
            case DOWN:
                car.accelerate( 0 );
                break;
            case RIGHT:
            case LEFT:
                car.turn( 0 );
                break;
            default:
                break;
        }
    }
}
