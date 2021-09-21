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

    private void addMutatedParent( List<IACar> cars, IACar parent )
    {
        cars.add( new IACar( mutate( parent ) ) );
    }


    /**
     * Crossover the parents and mutate them to obtain their children
     * FIXME: does not guarantee at all to obtain a population of size newPopulation
     * @param parents: selected parents from all the cars
     * @param newPopulation: size of the new population
     */
    public List<IACar> nextGeneration( List<IACar> parents, int newPopulation )
    {
        List<IACar> cars = new ArrayList<>();
        // parents = default parents + their crossover
        List<IACar> fullParents = new ArrayList<>();
        for ( int i = 0; i < parents.size(); i++ )
        {
            // add default parents
            IACar parent = parents.get( i );
            // FIXME: UNSURE: parents.add( new IACar( parent.getCopyNN() ) );
            fullParents.add( parent );

            // and crossover all parents
            for ( int j = 0; j < i; j++ )
            {
                IACar otherParent = parents.get( j );
                NeuralNetwork crossNN = crossover( parent, otherParent );
                IACar crossedParent = new IACar( crossNN );
                crossedParent.setCrossedParent();
                fullParents.add( crossedParent );
            }
        }
        int parentSize = fullParents.size();
        System.out.println(parentSize + " parent(s) after crossover");

        // Mutation = mutate all the parents and add them to the population
        // all parents get an equivalent size of children
        int oldPopulation = cars.size();
        int childrenMaxPopulation = newPopulation - parentSize;
        int childrenPerParent = childrenMaxPopulation / parentSize;
        System.out.println("new population: " + newPopulation + ", old population: " + cars.size() + ", parent size: " + parentSize + ", children per parent: " + childrenPerParent);

        int carsIndex = 0;
        for ( IACar parent : fullParents )
            for ( int j = 0; j < childrenPerParent; j++ )
                addMutatedParent( cars, parent );

        // if there is still room for new cars, complete the population
        if ( carsIndex < childrenMaxPopulation )
        {
            IACar mainParent = fullParents.get( 0 );
            for ( int i = carsIndex; i < childrenMaxPopulation; i++ )
                addMutatedParent( cars, mainParent );
        }

        // add the parents (NOT MUTATED) at the end so that they appear in front of the other cars
        cars.addAll( fullParents );

        return cars;
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

    // FIXME: FIND GOOD CROSSOVER FUNCTION
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
