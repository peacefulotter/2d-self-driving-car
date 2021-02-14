package com.peacefulotter.ml.ia;

import com.peacefulotter.ml.ia.activation.ActivationFunc;
import com.peacefulotter.ml.ia.activation.Activations;
import com.peacefulotter.ml.maths.Matrix2d;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Genetic
{
    private static final double CROSSOVER_RATE = 0.5d;
    private static final double MUTATION_INTENSITY = 2d;
    private static final double MUTATION_RATE = 0.03d;

    public static final int[] DIMENSIONS = new int[] {9, 20, 2};
    private static final ActivationFunc[] ACTIVATIONS = new ActivationFunc[] {
            Activations.ReLU, Activations.HyperTan
    };

    private final List<NeuralNetwork> nnList;

    public Genetic()
    {
        this.nnList = new ArrayList<>();
    }

    public void clear() { nnList.clear(); }

    public void createNN()
    {
        NeuralNetwork nn = new NeuralNetwork( DIMENSIONS, ACTIVATIONS );
        nnList.add(nn);
    }


    public Matrix2d simulateNN( int i, Matrix2d data )
    {
        return nnList.get( i ).predict( data );
    }

    public void nextGeneration( List<Integer> indices, int newPopulation )
    {
        // Crossover - parents + crossover parents
        List<NeuralNetwork> parents = new ArrayList<>();
        for (Integer i: indices)
            parents.add( nnList.get( i ) );
        for (int i = 0; i < indices.size() - 1; i++)
            parents.add( crossover(nnList.get(indices.get(i)), nnList.get(indices.get(i + 1))) );
        System.out.println(parents.size() + " parent(s) after crossover");

        // Mutation - parents + mutated children
        nnList.clear();
        for (NeuralNetwork parent: parents)
        {
            nnList.add( parent );
            for (int i = 0; i < newPopulation / parents.size(); i++)
            {
                nnList.add( mutate( parent ) );
            }
        }
        for (int i = 0; i < newPopulation % parents.size(); i++)
        {
            nnList.add( mutate( parents.get( 0 ) ) );
        }
    }


    private static Matrix2d crossing( Matrix2d a, Matrix2d b )
    {
        if ( Math.random() < CROSSOVER_RATE )
            return a.plus(b).div(2);
        else
            return a;
    }

    public static NeuralNetwork crossover(NeuralNetwork nn1, NeuralNetwork nn2)
    {
        NeuralNetwork nn = new NeuralNetwork(DIMENSIONS, ACTIVATIONS);
        for (int i = 1; i < DIMENSIONS.length; i++)
        {
            nn.setW( i, crossing(nn1.getW(i), nn2.getW(i)) );
            nn.setB( i, crossing(nn1.getB(i), nn2.getB(i)) );
        }
        return nn;
    }


    private static Matrix2d mutation( Matrix2d a )
    {
        return a.applyFunc( (mat, i, j) -> {
            if (Math.random() < MUTATION_RATE)
                mat.setAt(i, j, a.getAt( i, j ) + MUTATION_INTENSITY * (new Random().nextDouble() - 0.5f) );
            else
                mat.setAt( i, j, a.getAt( i, j ) );
        } );
    }

    public static NeuralNetwork mutate( NeuralNetwork parent )
    {
        NeuralNetwork nn = new NeuralNetwork(DIMENSIONS, ACTIVATIONS);
        for (int i = 1; i < DIMENSIONS.length; i++)
        {
            nn.setW(i, mutation( parent.getW(i) ));
            nn.setB(i, mutation( parent.getB(i) ));
        }
        return nn;
    }
}
