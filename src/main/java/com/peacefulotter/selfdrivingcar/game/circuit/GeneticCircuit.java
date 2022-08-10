package com.peacefulotter.selfdrivingcar.game.circuit;

import com.peacefulotter.selfdrivingcar.game.map.Maps;
import com.peacefulotter.selfdrivingcar.game.car.Car;
import com.peacefulotter.selfdrivingcar.game.map.Map;
import com.peacefulotter.selfdrivingcar.ml.IACar;
import com.peacefulotter.selfdrivingcar.ui.BottomPanel;
import com.peacefulotter.selfdrivingcar.ui.GenerationPanel;
import com.peacefulotter.selfdrivingcar.utils.Loader;
import com.peacefulotter.selfdrivingcar.ml.genetic.Genetic;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.List;
import java.util.stream.Collectors;

public class GeneticCircuit extends Circuit
{
    private static final int DIVERSITY_THRESHOLD = 3;

    private static final DoubleProperty averageSpeed = new SimpleDoubleProperty(0);
    private static final IntegerProperty selectedParents = new SimpleIntegerProperty(0);

    protected final Genetic genetic;

    private double speed;
    private int generation;
    private int deadCars;

    public GeneticCircuit( Map map, Genetic genetic )
    {
        super( map );
        this.genetic = genetic;

        for (int i = 0; i < genetic.getPopulation(); i++)
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
            System.out.println( "No parent selected, aborting next generation and rerunning this gen.");
            return;
        } else if ( parentsSize < DIVERSITY_THRESHOLD )
        {
            System.out.println( "You only have selected " + parentsSize + " parent(s), this might lead to poor diversity and thus long or no training in the long run");
        }

        // apply crossovers to the parents
        // and generate the new population by mutating the crossed parents
        List<IACar> newCars = genetic.nextGeneration( parents, generation + 1 );
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
        generation += 1;
        deadCars = 0;
        selectedParents.setValue( 0 );
        averageSpeed.setValue( 0 );
        GenerationPanel.setGen( generation );
    }

    // the selected cars become the parents for the next generation
    List<IACar> getGenParents()
    {
        return cars.stream()
            .filter(Car::isSelected)
            .collect(Collectors.toList());
    }

    @Override
    protected void update( double deltaTime, int from, int to )
    {
        super.update(deltaTime, from, to);

        for ( int i = from; i < to; i++ )
        {
            IACar car = cars.get( i );
            if ( car.isDead() && !car.isReset() )
            {
                deadCars += 1;
                continue;
            }
            speed += car.getSpeed();
        }
    }

    @Override
    public void render()
    {
        super.render();
        int population = cars.size();
        averageSpeed.setValue( speed / (float) population );
        BottomPanel.setPopProportion( population - deadCars, deadCars );
        speed = 0;
    }

    public void testGeneration()
    {
        map.setParams( Maps.TEST );
        List<IACar> parents = getGenParents();
        parents.forEach(IACar::resetCar);
        System.out.println("Testing on " + parents.size() + " parents");
        createGeneration( parents );
    }

    public void saveSelectedCar()
    {
        for ( IACar car: cars )
        {
            if ( car.isSelected() )
            {
                new Loader().saveModel( car.getCopyNN() );
                return;
            }
        }
    }

    public int getGeneration() { return generation; }
    public DoubleProperty getAverageSpeed() { return averageSpeed; }
    public IntegerProperty getSelectedParents() { return selectedParents; }
}
