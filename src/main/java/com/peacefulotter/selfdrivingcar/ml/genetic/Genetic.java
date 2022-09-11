package com.peacefulotter.selfdrivingcar.ml.genetic;

import com.peacefulotter.selfdrivingcar.maths.Matrix2d;
import com.peacefulotter.selfdrivingcar.ml.IACar;
import com.peacefulotter.selfdrivingcar.ml.NeuralNetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Genetic
{
    private final GeneticParams params;

    public Genetic( GeneticParams params )
    {
        this.params = params;
    }

    public Genetic( int population, double crossoverRate, double mutationStrength, double mutationRate )
    {
        this.params = new GeneticParams( population, crossoverRate, mutationStrength, mutationRate );
    }

    public GeneticParams getParams() { return params; }
    public int getPopulation() { return params.population; }
    public double getCrossoverRate() { return params.crossoverRate; }
    public double getMutationStrength() { return params.mutationStrength; }
    public double getMutationRate() { return params.mutationRate; }

    private void addMutatedParent(List<IACar> cars, IACar parent )
    {
        cars.add( new IACar( parent.nbArrows, parent.nbFutureArrows, mutate( parent ) ) );
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
            IACar copyParent = new IACar( parent.nbArrows, parent.nbFutureArrows, parent.getCopyNN() ).setParent();
            fullParents.add( copyParent ); // copy nn?

            // and crossover them
            for ( int j = 0; j < i; j++ )
            {
                IACar otherParent = parents.get( j );
                NeuralNetwork crossNN = crossover( parent, otherParent );
                IACar crossedParent = new IACar( parent.nbArrows, parent.nbFutureArrows, crossNN ).setCrossedParent();
                fullParents.add( crossedParent );
            }
        }
        return fullParents;
    }

    /**
     * Crossover the parents and mutate them to obtain their children
     * @param parents: selected parents from all the cars
     */
    public List<IACar> nextGeneration( List<IACar> parents, int gen )
    {
        List<IACar> cars = new ArrayList<>();
        List<IACar> fullParents = getCrossoverParents(parents);

        int parentSize = fullParents.size();
        int newPopulation = params.population;
        int childrenPopulation = newPopulation - parentSize;
        int childrenPerParent = childrenPopulation / parentSize;
        System.out.println("[" + gen + "] new population: " + newPopulation + ", parent size: " + parentSize + ", children per parent: " + childrenPerParent);

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
            if ( Math.random() < params.crossoverRate )
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
            if ( Math.random() < params.mutationRate )
                return a.getAt( i, j ) + params.mutationStrength * ( new Random().nextDouble() - 0.5f );
            else
                return a.getAt( i, j );
        } );
    }

    public NeuralNetwork mutate( IACar parent )
    {
        return parent.applyNNFunction( this::mutation );
    }
}
