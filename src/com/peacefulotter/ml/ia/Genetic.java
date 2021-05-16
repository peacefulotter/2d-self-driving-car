package com.peacefulotter.ml.ia;

import com.peacefulotter.ml.maths.Matrix2d;
import javafx.scene.control.SpinnerValueFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Genetic
{
    private final SpinnerValueFactory<Double> crossRate, mutStrength, mutRate;

    public Genetic( SpinnerValueFactory<Double> crossRate,
                    SpinnerValueFactory<Double> mutStrength,
                    SpinnerValueFactory<Double> mutRate )
    {
        this.crossRate = crossRate;
        this.mutStrength = mutStrength;
        this.mutRate = mutRate;
    }

    private void setOrAddMutatedParent( List<IACar> cars, int index, int population, IACar parent )
    {
        // if the car is already created, simply reset it and set its NN to the new mutated one
        if ( index < population )
        {
            IACar car = cars.get( index );
            car.resetCar();
            car.setNN( mutate( parent ) );
        }
        // if the car doesn't yet exist, create a new one
        else
            cars.add( new IACar( mutate( parent ) ) );
    }

    private void setOrAddParentCar( List<IACar> cars, int index, int population, IACar car )
    {
        car.resetCar();
        car.setParent( true );
        // if the car is already created, simply reset it and set its NN to the new mutated one
        if ( index < population )
            cars.set( index, car );
        // if the car doesn't yet exist, create a new one
        else
            cars.add( car );
    }

    /**
     * Crossover the parents and mutate them to obtain their children
     * FIXME: does not guarantee at all to obtain a population of size newPopulation
     * @param parentsIndex: indices in the cars list of the selected parents
     * @param cars: cars that drove the circuit
     * @param newPopulation: size of the new population
     */
    public void nextGeneration( List<Integer> parentsIndex, List<IACar> cars, int newPopulation )
    {
        // parents = default parents + their crossover
        List<IACar> parents = new ArrayList<>();
        for (int i = 0; i < parentsIndex.size(); i++)
        {
            // add default parents
            IACar parent = cars.get( parentsIndex.get( i ) );
            parents.add( new IACar( parent.getCopyNN() ) );

            // and crossover all parents
            for ( int j = 0; j < i; j++ )
            {
                NeuralNetwork crossNN = crossover(
                        cars.get( parentsIndex.get( i ) ),
                        cars.get( parentsIndex.get( j ) ) );
                parents.add( new IACar( crossNN ) );
            }
        }
        int parentSize = parents.size();
        System.out.println(parentSize + " parent(s) after crossover");

        // Mutation = mutate all the parents and add them to the population
        // all parents get an equivalent size of children
        int oldPopulation = cars.size();
        int childrenMaxPopulation = newPopulation - parentSize;
        int childrenPerParent = childrenMaxPopulation / parentSize;
        System.out.println("new population: " + newPopulation + ", old population: " + cars.size() + ", parent size: " + parentSize + ", children per parent: " + childrenPerParent);

        int carsIndex = 0;
        for ( IACar parent : parents )
            for ( int j = 0; j < childrenPerParent; j++ )
                setOrAddMutatedParent( cars, carsIndex++, oldPopulation, parent );

        // if there is still room for new cars, complete the population
        if ( carsIndex < childrenMaxPopulation )
        {
            IACar mainParent = parents.get( 0 );
            for ( int i = carsIndex; i < childrenMaxPopulation; i++ )
                setOrAddMutatedParent( cars, i, oldPopulation, mainParent );
        }

        // add the parents (NOT MUTATED)
        for ( int i = 0; i < parentSize; i++ )
            setOrAddParentCar( cars, i + childrenMaxPopulation, oldPopulation, parents.get( i ) );

        System.out.println("final size:  " + cars.size() );
    }


    /*private Matrix2d crossing( Matrix2d a, Matrix2d b )
    {
        return a.applyFunc( (mat, i, j) -> {
            if ( Math.random() < crossRate.getValue() )
                return ( a.getAt( i, j ) + b.getAt( i, j ) ) / 2d;
            else
                return a.getAt( i, j );
        } );
    }*/
    private Matrix2d crossing( Matrix2d a, Matrix2d b )
    {
        return a.applyFunc( (mat, i, j) -> {
            if ( Math.random() < crossRate.getValue() )
                return a.getAt( i, j );
            else
                return b.getAt( i, j );
        } );
    }

    public NeuralNetwork crossover( IACar car1, IACar car2 )
    {
        return car1.applyNNFunction( this::crossing, car2 );
    }


    private Matrix2d mutation( Matrix2d a )
    {
        return a.applyFunc( (mat, i, j) -> {
            if ( Math.random() < mutRate.getValue() )
                return a.getAt( i, j ) + mutStrength.getValue() * ( new Random().nextDouble() - 0.5f );
            else
                return a.getAt( i, j );
        } );
    }

    public NeuralNetwork mutate( IACar parent )
    {
        return parent.applyNNFunction( this::mutation );
    }
}
