package com.peacefulotter.selfdrivingcar.game.circuit;

import com.peacefulotter.selfdrivingcar.game.map.Maps;
import com.peacefulotter.selfdrivingcar.game.car.Car;
import com.peacefulotter.selfdrivingcar.game.map.Map;
import com.peacefulotter.selfdrivingcar.ml.IACar;
import com.peacefulotter.selfdrivingcar.utils.Loader;
import com.peacefulotter.selfdrivingcar.ml.genetic.Genetic;
import javafx.beans.property.*;

import java.util.List;
import java.util.stream.Collectors;

public class GeneticCircuit extends Circuit<IACar>
{
    private static final int DIVERSITY_THRESHOLD = 3;

    private static final DoubleProperty averageSpeed = new SimpleDoubleProperty( 0 );
    private static final IntegerProperty selectedParents = new SimpleIntegerProperty( 0 );
    private static final IntegerProperty generation = new SimpleIntegerProperty( 1 );
    private static final StringProperty popProportion = new SimpleStringProperty( "0 / 0" );

    protected final Genetic genetic;

    public GeneticCircuit( Map map, Genetic genetic )
    {
        super( map );
        this.genetic = genetic;

        for ( int i = 0; i < genetic.getPopulation(); i++ )
            addCarToCircuit( new IACar() );
    }

    public static void addSelected( int amount )
    {
        selectedParents.setValue( selectedParents.getValue() + amount );
    }

    public void nextGeneration( List<IACar> parents )
    {
        int parentsSize = parents.size();
        if ( parentsSize == 0 )
        {
            System.out.println( "No parent selected, aborting next generation and rerunning this gen." );
            return;
        } else if ( parentsSize < DIVERSITY_THRESHOLD )
        {
            System.out.println( "You only have selected " + parentsSize + " parent(s), this might lead to poor diversity and thus long or no training in the long run" );
        }

        // Change map
        map.setParams( generation.get() % 3 == 0 ? Maps.TEST : Maps.DEFAULT );

        // apply crossovers to the parents
        // and generate the new population by mutating the crossed parents
        List<IACar> newCars = genetic.nextGeneration( parents, generation.get() + 1 );
        createGeneration( newCars );
    }

    public void nextGeneration()
    {
        List<IACar> parents = getGenParents();
        nextGeneration( parents );
    }

    void createGeneration( List<IACar> newCars )
    {
        setCars( newCars );
        generation.set( generation.get() + 1 );
        selectedParents.setValue( 0 );
        averageSpeed.setValue( 0 );
        popProportion.setValue( cars.size() + " / " + cars.size() );
    }

    // the selected cars become the parents for the next generation
    List<IACar> getGenParents()
    {
        return cars.stream()
            .filter( Car::getIsSelected )
            .collect( Collectors.toList() );
    }

    @Override
    public void render()
    {
        super.render();

        int population = cars.size();
        int deadCars = 0;
        double speed = 0;
        for ( IACar car : cars )
        {
            if ( car.isDead() )
                deadCars += 1;
            speed += car.getSpeed();
        }

        averageSpeed.setValue( speed / (double) population );
        popProportion.setValue( population - deadCars + " / " + deadCars );
    }

    public void testGeneration()
    {
        map.setParams( Maps.TEST );
        List<IACar> parents = getGenParents();
        parents.forEach( IACar::resetCar );
        System.out.println( "Testing on " + parents.size() + " parents" );
        createGeneration( parents );
    }

    public void saveSelectedCar()
    {
        for ( IACar car : cars )
        {
            if ( car.getIsSelected() )
            {
                new Loader().saveModel( car );
                return;
            }
        }
    }

    public DoubleProperty getAverageSpeedProperty()
    {
        return averageSpeed;
    }

    public IntegerProperty getSelectedParentsProperty()
    {
        return selectedParents;
    }

    public IntegerProperty getGenerationProperty()
    {
        return generation;
    }

    public StringProperty getPopProportionProperty()
    {
        return popProportion;
    }
}
