package com.peacefulotter.ml.game.circuit;

import com.peacefulotter.ml.game.Map;
import com.peacefulotter.ml.ui.Spinners;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadCircuit extends Circuit
{
    private static final int THREAD_REDUCE = 2;
    public static final int THREADS = Runtime.getRuntime().availableProcessors() - THREAD_REDUCE; // nb of threads

    private final List<ThreadedCircuit> threadedCircuits;

    public MultiThreadCircuit(Map map, Spinners spinners )
    {
        super( map, spinners );

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

        System.out.println("Working on " + THREADS + " threads");
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
        threadedCircuits.forEach( tc -> tc.update(deltaTime) );
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

    @Override
    public void recordParentGeneration()
    {
        threadedCircuits.forEach( ThreadedCircuit::recordParentGeneration );
    }

    @Override
    public void saveRecordedParents()
    {
        threadedCircuits.forEach( ThreadedCircuit::saveRecordedParents );
    }
}
