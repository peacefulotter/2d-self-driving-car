package com.peacefulotter.selfdrivingcar.game.circuit;

/**
 * Handles a part of the circuit cars (only the update)
 */
public class ThreadedCircuit extends Thread
{
    private final GeneticCircuit circuit;

    private volatile boolean requestUpdate = false;
    private volatile double deltaTime;
    private int startPop, endPop;

    public ThreadedCircuit(GeneticCircuit circuit, int startPop, int endPop )
    {
        super();
        this.circuit = circuit;
        this.startPop = startPop;
        this.endPop = endPop;
    }

    public void updatePopulation( int startPop, int endPop )
    {
        System.out.println("Updating pop: " + startPop + " - " + endPop);
        this.startPop = startPop;
        this.endPop = endPop;
    }

    @Override
    public void run()
    {
        while ( true )
        {
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
            circuit.update( deltaTime, startPop, endPop );
            requestUpdate = false;
        }
    }

    public void update( double deltaTime )
    {
        this.deltaTime = deltaTime;
        requestUpdate = true;
    }
}
