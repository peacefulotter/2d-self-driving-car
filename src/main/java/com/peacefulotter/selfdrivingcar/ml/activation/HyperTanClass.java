package com.peacefulotter.selfdrivingcar.ml.activation;

import com.peacefulotter.selfdrivingcar.maths.Matrix2d;

class HyperTanClass extends ActivationFunc
{
    public HyperTanClass()
    {
        super("hypertan");
    }

    public Matrix2d forward(Matrix2d z ) {
        return z.applyFunc( ( mat, i, j) -> Math.tanh( z.getAt( i, j ) ) );
    }

    public Matrix2d gradient( Matrix2d z ) {
        Matrix2d f = forward( z );
        return f.applyFunc( (mat, i, j) -> 1 - Math.pow( f.getAt( i, j ), 2) );
    }
}
