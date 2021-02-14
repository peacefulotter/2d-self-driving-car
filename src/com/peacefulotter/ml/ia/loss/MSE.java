package com.peacefulotter.ml.ia.loss;

import com.peacefulotter.ml.maths.Matrix2d;

public class MSE implements LossFunc
{
    @Override
    public double loss( Matrix2d pred, Matrix2d y )
    {
        return pred.sub(y).pow(2).mean();
    }

    @Override
    public Matrix2d gradient( Matrix2d pred, Matrix2d y )
    {
        return pred.sub( y ).mul( 2d / (pred.rows * pred.cols) );
    }
}
