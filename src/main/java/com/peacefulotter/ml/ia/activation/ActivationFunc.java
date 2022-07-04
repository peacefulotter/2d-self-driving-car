package com.peacefulotter.ml.ia.activation;

import com.peacefulotter.ml.maths.Matrix2d;

public abstract class ActivationFunc
{
    public final String name;

    ActivationFunc( String name )
    {
        this.name = name;
    }

    public abstract Matrix2d forward(Matrix2d z);
    public abstract Matrix2d gradient(Matrix2d z);
}
