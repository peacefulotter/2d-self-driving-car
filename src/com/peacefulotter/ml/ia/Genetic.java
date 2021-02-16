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

    public void nextGeneration( List<IACar> cars, List<Integer> indices, int newPopulation )
    {
        // Crossover -> add parents + crossover parents
        // List<IACar> parents = new ArrayList<>();
        // for (Integer i: indices)
        //     parents.add( cars.get( i ) );
        /*for (int i = 0; i < indices.size() - 1; i++)
        {
            IACar crossCar = crossover(
                    cars.get(indices.get(i)),
                    cars.get(indices.get(i + 1)));
            parents.add( crossCar );
        }*/
        System.out.println(indices.size() + " parent(s) after crossover");

        // Mutation -> add mutated children to the population
        int j = 0;
        int oldPopulation = cars.size();
        for ( int i = 0; i < newPopulation; i++ )
        {
            if ( i % (newPopulation / indices.size()) == 0 && j < indices.size() )
                j++;
            if ( indices.contains( i ) )
                continue;
            NeuralNetwork mutatedNN = mutate( cars.get( indices.get( j - 1 ) ) );
            if ( i < oldPopulation)
            {
                cars.get( i ).resetCar();
                cars.get( i ).setNN( mutatedNN );
            }
            else
                cars.add( new IACar( mutatedNN ) );
        }
    }


    private Matrix2d crossing( Matrix2d a, Matrix2d b )
    {
        return a.applyFunc( (mat, i, j) -> {
            if (Math.random() < crossRate.getValue())
                return (a.getAt(i,j) + b.getAt(i,j)) / 2d;
            else
                return a.getAt( i, j );
        } );
    }

    public NeuralNetwork crossover(IACar car1, IACar car2)
    {
        return car1.applyNNFunction( this::crossing, car2 );
    }


    private Matrix2d mutation( Matrix2d a )
    {
        return a.applyFunc( (mat, i, j) -> {
            if (Math.random() < mutRate.getValue())
                return a.getAt( i, j ) + mutStrength.getValue() * (new Random().nextDouble() - 0.5f);
            else
                return a.getAt( i, j );
        } );
    }

    public NeuralNetwork mutate( IACar parent )
    {
        return parent.applyNNFunction( this::mutation );
    }
}
