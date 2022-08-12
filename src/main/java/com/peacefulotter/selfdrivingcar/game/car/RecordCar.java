package com.peacefulotter.selfdrivingcar.game.car;

import com.peacefulotter.selfdrivingcar.ml.IACar;

import java.util.List;

public class RecordCar extends IACar
{
    private static final double RECORD_PROBABILITY = 0.2;

    private final Record record = new Record();

    public RecordCar()
    {
        super(true);
    }

    @Override
    public void update(double deltaTime )
    {
        super.update( deltaTime );
        if ( Math.random() < RECORD_PROBABILITY )
            record.addSample( position, speed, acceleration );
    }

    public List<Record.Sample> getRecording() { return record.getSamples(); }
}
