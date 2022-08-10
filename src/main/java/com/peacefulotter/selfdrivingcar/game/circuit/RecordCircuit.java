package com.peacefulotter.selfdrivingcar.game.circuit;

import com.peacefulotter.selfdrivingcar.game.map.Map;
import com.peacefulotter.selfdrivingcar.game.car.RecordCar;
import com.peacefulotter.selfdrivingcar.ml.IACar;
import com.peacefulotter.selfdrivingcar.ml.genetic.Genetic;
import com.peacefulotter.selfdrivingcar.utils.Loader;

import java.util.List;
import java.util.stream.Collectors;

public class RecordCircuit extends GeneticCircuit
{
    public RecordCircuit(Map map, Genetic genetic) {
        super(map, genetic);
    }

    public void recordParentGeneration()
    {
        List<IACar> parents = getGenParents().stream().map(parent -> {
            RecordCar car = new RecordCar();
            car.setNN( parent.getCopyNN() );
            return car;
        } ).collect( Collectors.toList() );
        System.out.println( "Recording parents " + parents.size() );
        createGeneration( parents );
    }

    public void saveRecordedParents()
    {
        System.out.println("SAVE RECORDED " + cars.size() );
        for ( int i = 0; i < cars.size(); i++ )
        {
            Loader.saveRecording(  i, ((RecordCar) cars.get( i )).getRecording() );
        }
    }
}
