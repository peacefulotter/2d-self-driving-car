package com.peacefulotter.ml.game.circuit;

import com.peacefulotter.ml.Main;
import com.peacefulotter.ml.game.car.Car;
import com.peacefulotter.ml.game.Map;
import com.peacefulotter.ml.ia.IACar;
import com.peacefulotter.ml.utils.Loader;
import com.peacefulotter.ml.ia.Genetic;
import com.peacefulotter.ml.maths.Matrix2d;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.SpinnerValueFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Circuit
{
    // train one neural network before using GA
    // this uses supervised learning and gradient descent with a dataset I made playing the game
    private static final boolean TRAIN_BEFORE = false;
    private static final int DIVERSITY_THRESHOLD = 3;

    private static final DoubleProperty averageSpeed = new SimpleDoubleProperty(0);
    private static final IntegerProperty selectedParents = new SimpleIntegerProperty(0);

    private final Genetic genetic;
    protected final Map map;

    private List<IACar> cars;
    private double speed;
    private int generation;
    private int deadCars;

    protected int population;

    public Circuit( Map map, int population, Genetic genetic)
    {
        this.map = map;
        this.genetic = genetic;
        this.population = population;
        this.cars = new ArrayList<>();
        genCars();
    }

    public static void addSelected( int amount )
    {
        selectedParents.setValue( selectedParents.getValue() + amount );
    }

    public void genCars()
    {
        for (int i = 0; i < population; i++)
        {
            addCarToCircuit();
        }
    }

    private void addCarToCircuit()
    {
        IACar car = new IACar();
        cars.add( car );
        map.addCarToMap( car );
    }

    public void update( float deltaTime ) { update( deltaTime, 0, population ); }

    public void nextGeneration( int newPopulation )
    {
        List<IACar> parents = getGenParents();

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
        cars = genetic.nextGeneration( parents, newPopulation );

        // clear the map and add the new cars
        map.remove( 0, population );
        for ( IACar car: cars )
            map.addCarToMap( car );

        generation += 1;
        population = newPopulation;
        deadCars = 0;
        selectedParents.setValue( 0 );
        averageSpeed.setValue( 0 );
    }

    // the selected cars become the parents for the next generation
    private List<IACar> getGenParents()
    {
        return cars.stream()
                .filter(Car::isSelected)
                .collect(Collectors.toList());
    }

    protected void update( float deltaTime, int from, int to )
    {
        for ( int i = from; i < to; i++ )
        {
            IACar car = cars.get( i );
            if ( car.isDead() && !car.isReset() )
            {
                deadCars += 1;
                car.partialReset();
                continue;
            }

            // get the output from the NN
            Matrix2d output = car.simulate();
            double throttle = output.getAt( 0, 0 );
            double turn = output.getAt( 0, 1 );

            // and apply it to the car
            car.accelerate( throttle );
            car.turn( turn );
            car.update( deltaTime );

            speed += car.getSpeed();
        }
    }

    public void render()
    {
        averageSpeed.setValue( speed / (float) population );
        speed = 0;

        Main.setPopulationProportion( population - deadCars, deadCars );
        map.render( cars );
    }

    public void testGeneration()
    {
        for (int i = population - 1; i >= 0; i--) {
            IACar car = cars.get( i );
            if ( car.isSelected() )
            {
                car.resetCar();
                car.setParent();
            }
            else
            {
                map.remove( i );
            }
        }
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
