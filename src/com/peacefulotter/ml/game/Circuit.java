package com.peacefulotter.ml.game;

import com.peacefulotter.ml.ia.IACar;
import com.peacefulotter.ml.utils.Loader;
import com.peacefulotter.ml.ia.Genetic;
import com.peacefulotter.ml.maths.Matrix2d;
import com.peacefulotter.ml.maths.Vector2d;
import com.peacefulotter.ml.utils.Input;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;


public class Circuit
{
    // train one neural network before using GA
    // this uses supervised learning and gradient descent with a dataset I made playing the game
    private static final boolean TRAIN_BEFORE = true;
    private static final boolean USER_CONTROL = false;
    private static final int DIVERSITY_THRESHOLD = 4;

    private static final List<Matrix2d> positions = new ArrayList<>();
    private static final List<Vector2d> controls = new ArrayList<>();
    
    private final DoubleProperty averageSpeed = new SimpleDoubleProperty(0);
    private final List<IACar> cars;
    private final Genetic genetic;
    private final Map map;

    private int generation;
    private int population;
    private double speed;

    public Circuit( Map map, int population,
                    SpinnerValueFactory<Double> crossRate,
                    SpinnerValueFactory<Double> mutStrength,
                    SpinnerValueFactory<Double> mutRate )
    {
        this.map = map;
        this.genetic = new Genetic(crossRate, mutStrength, mutRate);
        this.population = population;
        this.cars = new ArrayList<>();
        genCars();
    }

    protected void addCarToMap( ImageView img )
    {
        map.addCarToMap( img );
    }

    public void genCars()
    {
        for (int i = 0; i < population; i++)
        {
            IACar car = new IACar();
            cars.add( car );
            addCarToMap( car.getCarImgView() );
        }
    }

    public void update( float deltaTime ) { update( deltaTime, 0, population ); }

    public void nextGeneration( int newPopulation )
    {
        List<Integer> indices = new ArrayList<>();
        // the selected cars become the parents of the next generation
        for (int i = 0; i < population; i++) {
            Car car = cars.get( i );
            if ( car.isSelected() )
            {
                indices.add( i );
                car.resetCar();
                car.setParent( true );
            }
        }

        if ( indices.size() == 0 )
        {
            System.out.println( "No parent selected, aborting next generation");
            return;
        } else if ( indices.size() < DIVERSITY_THRESHOLD )
        {
            System.out.println( "You only have selected " + indices.size() + " parent(s), this might lead to poor diversity and thus long or no training in the long run");
        }

        // apply crossovers and mutations
        genetic.nextGeneration( cars, indices, newPopulation );

        // if new population < old population, remove unnecessary cars
        if ( newPopulation < population )
        {
            cars.subList( newPopulation, population ).clear();
            map.remove( newPopulation + 2, population + 2 );
        }
        // if the new population is bigger, add the new cars to the circuit
        else if ( newPopulation > population )
            for ( int i = population; i < newPopulation; i++ )
                addCarToMap(cars.get( i ).getCarImgView());

        generation += 1;
        population = newPopulation;
    }

    protected void update(float deltaTime, int from, int to)
    {
        if (USER_CONTROL)
        {
            Matrix2d x = cars.get( 0 ).simulate();
            Vector2d y = Input.getVector();
            positions.add( x );
            controls.add( y.copy() );
            if (positions.size() > 3500)
            {
                Loader.saveDrivingData( positions, controls );
                System.out.println("SAVED");
                positions.clear();
                controls.clear();
            }
            return;
        }

        for (int i = from; i < to; i++)
        {
            IACar car = cars.get( i );
            Matrix2d output = car.simulate();
            double throttle = output.getAt( 0, 0 );
            double turn = output.getAt( 0, 1 );
            car.accelerate( throttle );
            car.turn( turn );
            car.update(deltaTime);
            speed += car.getSpeed();
        }
    }

    public void render()
    {
        averageSpeed.setValue( speed / population );
        map.render( cars );
        speed = 0;
    }

    public int getGeneration() { return generation; }
    public int getPopulation() { return population; }
    public DoubleProperty getAverageSpeed() { return averageSpeed; }
}
