package com.peacefulotter.selfdrivingcar.game.circuit;

import com.peacefulotter.selfdrivingcar.game.map.Map;
import com.peacefulotter.selfdrivingcar.ml.genetic.Genetic;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadCircuit extends GeneticCircuit
{
    private static final int THREAD_REDUCE = 2;
    public static final int THREADS = Runtime.getRuntime().availableProcessors() - THREAD_REDUCE; // nb of threads

    private final List<ThreadedCircuit> threadedCircuits;

    private boolean isTesting;

    public MultiThreadCircuit( Map map, Genetic genetic )
    {
        super( map, genetic );

        this.threadedCircuits = new ArrayList<>();

        int population = genetic.getPopulation();
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
    public void update( double deltaTime )
    {
        // TODO: need testing anymore?
        if ( isTesting )
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
        int oldPopulation = cars.size();
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
}
