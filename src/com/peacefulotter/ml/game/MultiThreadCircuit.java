package com.peacefulotter.ml.game;

import javafx.scene.control.SpinnerValueFactory;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadCircuit extends Circuit
{
    private static final int THREAD_REDUCE = 2;
    public static final int THREADS = Runtime.getRuntime().availableProcessors() - THREAD_REDUCE; // nb of threads

    private final List<ThreadedCircuit> threadedCircuits;

    public MultiThreadCircuit( Map map, int population,
                               SpinnerValueFactory<Double> crossRate,
                               SpinnerValueFactory<Double> mutStrength,
                               SpinnerValueFactory<Double> mutRate )
    {
        super( map, population, crossRate, mutStrength, mutRate );

        this.threadedCircuits = new ArrayList<>();

        int padding = population / THREADS;
        int startPop = 0;
        int endPop = padding;
        for ( int i = 0; i < THREADS - 1; i++ )
        {
            addThreadedCircuit( startPop, endPop );
            startPop += padding;
            endPop += padding;
        }

        int rest = population % THREADS;
        addThreadedCircuit( startPop, endPop + rest );
    }

    private void addThreadedCircuit( int startPop, int endPop )
    {
        ThreadedCircuit tc = new ThreadedCircuit( this, startPop, endPop );
        threadedCircuits.add( tc );
        tc.setDaemon( true );
        tc.start();
    }

    public void setPopulation( int population )
    {
        int padding = population / THREADS;
        int startPop = 0;
        int endPop = padding;
        for ( int i = 0; i < THREADS - 1; i++ )
        {
            threadedCircuits.get( i ).updatePopulation( startPop, endPop );
            startPop += padding;
            endPop += padding;
        }

        int rest = population % THREADS;
        threadedCircuits.get( THREADS - 1 ).updatePopulation( startPop, endPop + rest );
    }

    @Override
    public void update( float deltaTime )
    {
        for ( ThreadedCircuit tc: threadedCircuits )
        {
            tc.update( deltaTime );
        }
    }

    @Override
    public void nextGeneration( int newPopulation )
    {
        int oldPopulation = population;
        super.nextGeneration( newPopulation );

        if ( newPopulation != oldPopulation )
        {
            setPopulation( newPopulation );
        }
    }
}
