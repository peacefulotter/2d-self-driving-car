package com.peacefulotter.selfdrivingcar.ml.loss;

import com.peacefulotter.selfdrivingcar.maths.Matrix2d;

public class Loss
{
    public static final LossFunc MSE = new MSEClass();

    private static class MSEClass implements LossFunc
    {
        public double loss( Matrix2d pred, Matrix2d y )
        {
            return pred.sub(y).pow(2).mean();
        }

        public Matrix2d gradient( Matrix2d pred, Matrix2d y )
        {
            return pred.sub( y ).mul( 2d / (pred.rows * pred.cols) );
        }
    }

}
