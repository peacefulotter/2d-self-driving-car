package com.peacefulotter.ml.ia.activation;

public enum Activations
{
    ReLU( new ReLUClass() ),
    Sigmoid( new SigmoidClass() ),
    HyperTan( new HyperTanClass() );

    private final ActivationFunc func;

    Activations( ActivationFunc func ) {
        this.func = func;
    }

    public ActivationFunc getFunc() { return func; }

    public static Activations getActivation(String name)
    {
        for (Activations activation: values() )
        {
            if ( activation.func.name.contentEquals( name ) )
            {
                return activation;
            }
        }

        return ReLU;
    }
}
