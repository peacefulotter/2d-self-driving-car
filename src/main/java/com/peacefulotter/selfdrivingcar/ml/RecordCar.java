package com.peacefulotter.selfdrivingcar.ml;

import java.util.List;

public class RecordCar extends IACar
{
    private static final double RECORD_PROBABILITY = 0.2;

    private final Record record = new Record();

    public RecordCar()
    {
        super(5, true);
    }

    @Override
    public void update( float deltaTime )
    {
        super.update( deltaTime );
        if ( Math.random() < RECORD_PROBABILITY )
            record.addSample( position, speed, acceleration );
    }

    public List<Record.Sample> getRecording() { return record.getSamples(); }
}
