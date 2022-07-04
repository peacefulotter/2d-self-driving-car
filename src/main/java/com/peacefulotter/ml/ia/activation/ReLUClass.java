package com.peacefulotter.ml.ia.activation;

import com.peacefulotter.ml.maths.Matrix2d;

class ReLUClass extends ActivationFunc
{
    public ReLUClass()
    {
        super("relu");
    }

    public Matrix2d forward(Matrix2d z ) {
        return z.applyFunc( (mat, i, j) -> {
            if ( z.getAt( i, j ) < 0 )
                return 0;
            else
                return z.getAt( i, j );
        } );
    }

    public Matrix2d gradient( Matrix2d z ) {
        return z.applyFunc( (mat, i, j) -> {
            if ( mat.getAt( i, j ) > 0 )
                return  1;
            else
                return 0;
        } );
    }
}
