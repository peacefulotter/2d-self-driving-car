package com.peacefulotter.ml.utils;

import com.peacefulotter.ml.game.car.Car;
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

        switch (key) {
            case UP -> {
                car.accelerate(1);
                vector.setX(1);
            }
            case RIGHT -> {
                car.turn(1);
                vector.setY(1);
            }
            case DOWN -> {
                car.accelerate(-1);
                vector.setX(-1);
            }
            case LEFT -> {
                car.turn(-1);
                vector.setY(-1);
            }
            default -> {
            }
        }
    }

    public static void handleKeyReleased( Car car, KeyEvent keyEvent )
    {
        KeyCode key = keyEvent.getCode();
        keyEvent.consume(); // stops the event propagation.

        switch (key) {
            case UP, DOWN -> {
                car.accelerate(0);
                vector.setX(0);
            }
            case RIGHT, LEFT -> {
                car.turn(0);
                vector.setY(0);
            }
            default -> {
            }
        }
    }

    public static Vector2d getVector() { return vector; }
}
