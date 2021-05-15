package com.peacefulotter.ml.game;

import com.peacefulotter.ml.ia.IACar;
import javafx.scene.control.SpinnerValueFactory;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadCircuit extends Circuit
{
    // FIXME: add a thread reduce value because having as many threads as cores is too much (to test)
    private static final int THREAD_REDUCE = 0;
    public static final int THREADS = Runtime.getRuntime().availableProcessors() - THREAD_REDUCE; // nb of threads

    private final List<ThreadedCircuit> threadedCircuits;

    private static class ThreadedCircuit extends Thread
    {
        private final Circuit circuit;
        private final int threadNumber;

        private volatile boolean requestUpdate = false;
        private volatile float deltaTime;
        private volatile int population;

        public ThreadedCircuit( Circuit circuit, int threadNumber )
        {
            super();
            this.circuit = circuit;
            this.threadNumber = threadNumber;
        }

        @Override
        public void run()
        {
            while(true) {
                triggerAction();
                if (Thread.interrupted()) { return; }
                try { sleep( 1 ); }
                catch ( InterruptedException e ) { e.printStackTrace(); }
            }
        }

        private void triggerAction()
        {
            if ( requestUpdate )
            {
                circuit.update( deltaTime, threadNumber * population, (threadNumber + 1) * population );
                requestUpdate = false;
            }
        }

        public void update( float deltaTime, int population )
        {
            this.deltaTime = deltaTime;
            this.population = population;
            requestUpdate = true;
        }
    }

    public MultiThreadCircuit( Map map, int population,
                               SpinnerValueFactory<Double> crossRate,
                               SpinnerValueFactory<Double> mutStrength,
                               SpinnerValueFactory<Double> mutRate )
    {
        super( map, population, crossRate, mutStrength, mutRate );

        this.threadedCircuits = new ArrayList<>();
        for ( int i = 0; i < THREADS; i++ )
        {
            ThreadedCircuit tc = new ThreadedCircuit( this, i );
            threadedCircuits.add( tc );
            tc.setDaemon( true );
            tc.start();
        }
    }

    @Override
    public void update( float deltaTime )
    {
        for ( int i = 0; i < THREADS; i++ )
        {
            threadedCircuits.get( i ).update( deltaTime, getPopulation() / THREADS );
        }
    }
}
