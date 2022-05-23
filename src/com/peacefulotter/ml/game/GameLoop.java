package com.peacefulotter.ml.game;

import com.peacefulotter.ml.Main;
import com.peacefulotter.ml.game.circuit.Circuit;
import com.peacefulotter.ml.utils.Time;
import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer
{
    private static final double FRAMES_CAP = 240;
    private static final double FRAME_TIME = 1.0 / FRAMES_CAP;

    private final Circuit circuit;

    private int frames;
    private double framesCounter, lastTime, unprocessedTime;

    public GameLoop( Circuit circuit )
    {
        this.circuit = circuit;
    }

    @Override
    public void start()
    {
        frames = 0;
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
            circuit.update( (float) FRAME_TIME );

            if (framesCounter >= Time.SECOND)
            {
                Main.setFPS(frames);
                frames = 0;
                framesCounter = 0;
            }
        }

        if ( render )
        {
            circuit.render();
            frames++;
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
