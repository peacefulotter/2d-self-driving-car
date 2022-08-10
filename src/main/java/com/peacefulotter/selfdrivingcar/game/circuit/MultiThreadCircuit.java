package com.peacefulotter.selfdrivingcar.game.circuit;

import com.peacefulotter.selfdrivingcar.game.Map;
import com.peacefulotter.selfdrivingcar.ml.genetic.Genetic;
import com.peacefulotter.selfdrivingcar.ml.genetic.GeneticParams;
import com.peacefulotter.selfdrivingcar.ui.Spinners;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadCircuit extends Circuit
{
    private static final int THREAD_REDUCE = 2;
    public static final int THREADS = Runtime.getRuntime().availableProcessors() - THREAD_REDUCE; // nb of threads

    private final List<ThreadedCircuit> threadedCircuits;

    private boolean isRecording;
    private boolean isTesting;

    public MultiThreadCircuit( Map map, Genetic genetic )
    {
        super( map, genetic );

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
        if ( isRecording )
        {
            super.update( deltaTime );
        }
        else if ( isTesting )
        {
            threadedCircuits.get(0).update(deltaTime);
        }
        else
        {
            threadedCircuits.forEach( tc -> tc.update( deltaTime ) );
        }
    }

    @Override
    public void nextGeneration()
    {
        int oldPopulation = population;
        super.nextGeneration();

        int newPopulation = genetic.getPopulation();
        if ( newPopulation != oldPopulation )
            setPopulation( newPopulation );
    }

    @Override
    public void testGeneration() {
        super.testGeneration();
        threadedCircuits.get(0).updatePopulation(0, cars.size());
        isTesting = true;
    }

    @Override
    public void recordParentGeneration()
    {
        super.recordParentGeneration();
        isRecording = true;
    }

    @Override
    public void saveRecordedParents()
    {
        super.saveRecordedParents();
        isRecording = false;
    }
}
