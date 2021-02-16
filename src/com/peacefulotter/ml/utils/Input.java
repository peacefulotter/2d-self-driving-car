package com.peacefulotter.ml.utils;

import com.peacefulotter.ml.game.Car;
import com.peacefulotter.ml.maths.Vector2d;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public abstract class Input
{
    private static final Vector2d vector = new Vector2d( 0, 0 );

    public static void handleKeyPressed( Car car, KeyEvent keyEvent )
    {
        KeyCode key = keyEvent.getCode();
        keyEvent.consume(); // stops the event propagation.

        switch ( key )
        {
            case UP:
                car.accelerate( 1 );
                vector.setX( 1 );
                break;
            case RIGHT:
                car.turn( 1 );
                vector.setY( 1 );
                break;
            case DOWN:
                car.accelerate( -1 );
                vector.setX( -1 );
                break;
            case LEFT:
                car.turn( -1 );
                vector.setY( -1 );
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
                vector.setX( 0 );
                break;
            case RIGHT:
            case LEFT:
                car.turn( 0 );
                vector.setY( 0 );
                break;
            default:
                break;
        }
    }

    public static Vector2d getVector() { return vector; }
}
