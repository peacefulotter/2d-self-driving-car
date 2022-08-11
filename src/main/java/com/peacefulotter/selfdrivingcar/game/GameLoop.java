package com.peacefulotter.selfdrivingcar.game;

import com.peacefulotter.selfdrivingcar.game.circuit.Circuit;
import com.peacefulotter.selfdrivingcar.utils.Time;
import javafx.animation.AnimationTimer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class GameLoop extends AnimationTimer
{
    private static final double FRAMES_CAP = 100;
    private static final double FRAME_TIME = 1.0 / FRAMES_CAP;
    public static final IntegerProperty frames = new SimpleIntegerProperty(0);

    private final Circuit circuit;

    private double framesCounter, lastTime, unprocessedTime;
    private int _frames = 0;

    public GameLoop( Circuit circuit )
    {
        this.circuit = circuit;
    }

    @Override
    public void start()
    {
        framesCounter = 0;
        lastTime = Time.getNanoTime();
        unprocessedTime = 0; // time in seconds since the start

        super.start();
    }

    @Override
    public void handle( long now )
    {
        boolean render = false;

        double startTime = Time.getNanoTime(); // delta between two updates
        double passedTime = startTime - lastTime;
        lastTime = startTime;
        unprocessedTime += passedTime / Time.SECOND;
        framesCounter += passedTime;

        while ( unprocessedTime > FRAME_TIME )
        {
            unprocessedTime -= FRAME_TIME;
            render = true;
            circuit.update( FRAME_TIME );

            if (framesCounter >= Time.SECOND)
            {
                frames.setValue( _frames );
                framesCounter = 0;
                _frames = 0;
            }
        }

        if ( render )
        {
            circuit.render();
            _frames++;
        } else
        {
            try
            {
                Thread.sleep( 1 );
            } catch ( InterruptedException e )
            {
                e.printStackTrace();
            }
        }
    }
}
