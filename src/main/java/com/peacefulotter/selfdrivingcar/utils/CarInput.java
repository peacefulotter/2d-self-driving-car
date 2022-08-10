package com.peacefulotter.selfdrivingcar.utils;

import com.peacefulotter.selfdrivingcar.game.car.Car;
import com.peacefulotter.selfdrivingcar.maths.Vector2d;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public abstract class CarInput
{
    private static final Vector2d vector = new Vector2d( 0, 0 );

    public static void handleKeyPressed( Car car, KeyEvent keyEvent )
    {
        KeyCode key = keyEvent.getCode();
        keyEvent.consume(); // stops the event propagation.

        switch (key) {
            case Z -> {
                car.accelerate(1);
                vector.setX(1);
            }
            case D -> {
                car.turn(1);
                vector.setY(1);
            }
            case S -> {
                car.accelerate(-1);
                vector.setX(-1);
            }
            case Q -> {
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
            case Z, S -> {
                car.accelerate(0);
                vector.setX(0);
            }
            case D, Q -> {
                car.turn(0);
                vector.setY(0);
            }
            default -> {
            }
        }
    }

    public static Vector2d getVector() { return vector; }
}
