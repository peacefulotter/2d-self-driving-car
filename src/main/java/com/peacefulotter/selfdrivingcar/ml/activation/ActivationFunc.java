package com.peacefulotter.selfdrivingcar.ml.activation;

import com.peacefulotter.selfdrivingcar.maths.Matrix2d;

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
