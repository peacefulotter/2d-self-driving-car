package com.peacefulotter.ml.ia;

import com.peacefulotter.ml.maths.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class Record
{
    private final List<Sample> samples;

    public Record()
    {
        this.samples = new ArrayList<>();
    }

    public void addSample( Vector2d position, double speed, double acceleration )
    {
        samples.add( new Sample( position.getX(), position.getY(), speed, acceleration) );
    }

    public List<Sample> getSamples() { return samples; }

    public static class Sample
    {
        protected final double x, y, speed, acceleration;

        private Sample( double x, double y, double speed, double acceleration )
        {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.acceleration = acceleration;
        }

        public String convertToCSV() {
            return x + "," + y + "," + speed + "," + acceleration;
        }

        @Override
        public String toString()
        {
            return convertToCSV();
        }
    }
}
