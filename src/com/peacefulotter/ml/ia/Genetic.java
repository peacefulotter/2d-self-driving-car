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


    private List<IACar> getCrossoverParents( List<IACar> parents )
    {
        // full parents = parents + their crossover
        List<IACar> fullParents = new ArrayList<>();
        int nbParents = parents.size();
        for ( int i = 0; i < nbParents; i++ )
        {
            // add default parents
            IACar parent = parents.get( i );
            IACar copyParent = new IACar( parent.getCopyNN() ).setParent();
            fullParents.add( copyParent ); // copy nn?

            // and crossover them
            for ( int j = 0; j < i; j++ )
            {
                IACar otherParent = parents.get( j );
                NeuralNetwork crossNN = crossover( parent, otherParent );
                IACar crossedParent = new IACar( crossNN ).setCrossedParent();
                fullParents.add( crossedParent );
            }
        }
        return fullParents;
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
        List<IACar> fullParents = getCrossoverParents(parents);

        int parentSize = fullParents.size();
        int childrenPopulation = newPopulation - parentSize;
        int childrenPerParent = childrenPopulation / parentSize;
        System.out.println("new population: " + newPopulation + ", parent size: " + parentSize + ", children per parent: " + childrenPerParent);

        // Mutation = mutate all the parents and add them to the population
        // all parents get an equivalent size of children
        int i = 0;
        while ( i++ < childrenPopulation )
        {
            IACar parent = fullParents.get( i % parentSize );
            addMutatedParent( cars, parent );
        }

        // add the parents at the end to make them appear in front of the others
        cars.addAll( fullParents );

        return cars;
    }

    // TODO: TEST MULTIPLE CROSSOVER FUNCTIONS

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
