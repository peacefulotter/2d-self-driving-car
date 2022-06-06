package com.peacefulotter.ml.ia;

import java.util.List;

public class RecordCar extends IACar
{
    private final Record record = new Record();

    @Override
    public void update( float deltaTime )
    {
        super.update( deltaTime );
        record.addSample( position, speed, acceleration );
    }

    public List<Record.Sample> getRecording() { return record.getSamples(); }
}
