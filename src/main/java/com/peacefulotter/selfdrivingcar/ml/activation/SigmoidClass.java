package com.peacefulotter.selfdrivingcar.ml.activation;

import com.peacefulotter.selfdrivingcar.maths.Matrix2d;

class SigmoidClass extends ActivationFunc
{
    public SigmoidClass()
    {
        super("sigmoid");
    }

    public Matrix2d forward(Matrix2d z ) {
        return z.applyFunc( (mat, i, j) -> 1 / (1 + Math.exp(-z.getAt( i, j ))) );
    }

    public Matrix2d gradient( Matrix2d z ) {
        Matrix2d f = forward( z );
        return f.applyFunc( (mat, i, j) -> {
            double elt = f.getAt( i, j );
            return elt * (1 - elt);
        } );
    }
}

