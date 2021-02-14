package com.peacefulotter.ml.ia.activation;

import com.peacefulotter.ml.maths.Matrix2d;

public class Activations
{
    public static final ActivationFunc ReLU = new ReLUClass();
    public static final ActivationFunc Sigmoid = new SigmoidClass();
    public static final ActivationFunc HyperTan = new HyperTanClass();

    private static class ReLUClass implements ActivationFunc {

        public Matrix2d forward( Matrix2d z ) {
            return z.applyFunc( (mat, i, j) -> {
                if ( z.getAt( i, j ) < 0 )
                    mat.setAt( i, j, 0 );
                else
                    mat.setAt( i, j, z.getAt( i, j ) );
            } );
        }

        public Matrix2d gradient( Matrix2d z ) {
            return z.applyFunc( (mat, i, j) -> {
                if ( mat.getAt( i, j ) > 0 )
                    mat.setAt( i, j, 1 );
            } );
        }
    }

    private static class SigmoidClass implements ActivationFunc {

        public Matrix2d forward( Matrix2d z ) {
            return Matrix2d.applyFunc( ( mat, i, j) -> {
                mat.setAt( i, j, 1 / (1 + Math.exp(-z.getAt( i, j ))) );
            }, z.rows, z.cols );
        }

        public Matrix2d gradient( Matrix2d z ) {
            Matrix2d f = forward( z );
            return f.applyFunc( (mat, i, j) -> {
                double elt = f.getAt( i, j );
                mat.setAt( i, j, elt * (1 - elt) );
            } );
        }
    }

    private static class HyperTanClass implements ActivationFunc
    {
        public Matrix2d forward( Matrix2d z ) {
            return z.applyFunc( ( mat, i, j) -> {
                mat.setAt( i, j, Math.tanh( z.getAt( i, j ) ) );
            } );
        }

        public Matrix2d gradient( Matrix2d z ) {
            Matrix2d f = forward( z );
            return f.applyFunc( (mat, i, j) -> {
                mat.setAt( i, j, 1 - Math.pow( f.getAt( i, j ), 2) );
            } );
        }
    }
}
